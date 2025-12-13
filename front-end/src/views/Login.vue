<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <el-text type="primary">
            {{ $t('login.title') }}
          </el-text>
        </div>
      </template>
      <div class="language-switch">
        <el-select 
          v-model="currentLocale" 
          @change="changeLanguage"
          size="small"
        >
          <el-option 
            v-for="locale in locales" 
            :key="locale.value" 
            :label="locale.label" 
            :value="locale.value"
          />
        </el-select>
      </div>
      <el-form class="login-form" label-position="left" :label-width="80" @keyup.enter="handleLogin">
        <el-form-item :label="$t('login.username')">
          <el-input v-model="loginForm.userName" :placeholder="$t('login.username')" clearable class="login-input" />
        </el-form-item>
        <el-form-item :label="$t('login.password')">
          <el-input v-model="loginForm.userPassword" type="password" :placeholder="$t('login.password')" show-password
            class="login-input" />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="rememberMe" @change="handleRememberMeChange">{{ $t('login.rememberMe') }}</el-checkbox>
        </el-form-item>
        <el-form-item class="button-form-item">
          <el-button type="primary" class="login-button" @click="handleLogin">
            {{ $t('login.loginButton') }}
          </el-button>
        </el-form-item>
      </el-form>
      <div class="register-link">
        <el-text>{{ $t('login.noAccount') }}</el-text>
        <el-button type="primary" link @click="goToRegister">
          {{ $t('login.registerNow') }}
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'
import { clearUserInfoCache } from '@/utils/user'
import { useI18n } from 'vue-i18n'

const { locale } = useI18n()

const loginForm = ref({
  userName: '',
  userPassword: ''
})

const rememberMe = ref(false)

const router = useRouter()

// 语言切换相关
const currentLocale = ref(localStorage.getItem('locale') || 'zh-CN')
const locales = ref([
  { label: '中文', value: 'zh-CN' },
  { label: 'English', value: 'en-US' }
])

const changeLanguage = (newLocale) => {
  locale.value = newLocale
  localStorage.setItem('locale', newLocale)
}

// 页面加载时检查是否有记住的登录信息
onMounted(() => {
  const savedCredentials = localStorage.getItem('rememberedCredentials')
  if (savedCredentials) {
    try {
      const credentials = JSON.parse(savedCredentials)
      loginForm.value.userName = credentials.userName || ''
      loginForm.value.userPassword = credentials.userPassword || ''
      rememberMe.value = true
    } catch (e) {
      console.error('解析记住的凭据时出错:', e)
    }
  }
})

// 处理"记住我"选项变化
const handleRememberMeChange = (checked) => {
  if (!checked) {
    // 如果用户取消勾选"记住我"，则清除已保存的凭据
    localStorage.removeItem('rememberedCredentials')
  }
}

async function login() {
  // 登录前清除旧的用户信息缓存
  clearUserInfoCache()
  
  // 清除localStorage中的旧用户信息
  localStorage.removeItem('userId')
  localStorage.removeItem('userName')
  
  try {
    // 确保发送的数据格式正确
    const loginData = {
      userName: loginForm.value.userName,
      userPassword: loginForm.value.userPassword
    };
    
    console.log('发送登录请求:', loginData);
    
    const res = await request.post('/user/login', loginData)
    console.log('登录响应:', res);
    
    // 注意：现在业务逻辑错误不会进入catch块，而是正常返回
    if (res.code === 1) {
      // 如果用户选择了"记住我"，则保存凭据
      if (rememberMe.value) {
        const credentials = {
          userName: loginForm.value.userName,
          userPassword: loginForm.value.userPassword
        }
        localStorage.setItem('rememberedCredentials', JSON.stringify(credentials))
      } else {
        // 如果没有选择"记住我"，确保清除已保存的凭据
        localStorage.removeItem('rememberedCredentials')
      }
      
      // 存储token
      localStorage.setItem('token', res.data?.token || res.token)
      // 存储用户名
      localStorage.setItem('userName', loginForm.value.userName)
      // 存储用户ID（如果后端返回了）
      if (res.data?.userId !== undefined && res.data?.userId !== null) {
        localStorage.setItem('userId', res.data.userId)
      }
      return true // 登录成功返回true
    } else if (res.code === 0) {
      // 只显示后端返回的错误信息，如果没有则显示默认信息
      if (res.msg) {
        ElMessage.error(res.msg)
      } else {
        ElMessage.error('登录失败：用户名或密码错误')
      }
      return false // 登录失败返回false
    } else {
      // 只显示后端返回的错误信息，如果没有则显示默认信息
      if (res.msg) {
        ElMessage.error(res.msg)
      } else {
        ElMessage.error('未知错误')
      }
      return false
    }
  } catch (e) {
    console.error('登录失败：', e)
    ElMessage.error('网络连接失败，请检查网络')
    return false // 异常时也返回false
  }
}

const loginResult = ref(null)

const handleLogin = async () => {
  loginResult.value = await login()
  // 调用login()，根据返回的状态决定是否跳转

  if (loginResult.value) { // 只有登录成功才执行后续操作
    ElMessage.success('登录成功！')
    router.push('/home')
  } else {
    loginForm.value.userPassword = ''
  }
}


const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.login-card {
  width: 100%;
  max-width: 400px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease-in-out;
  position: relative;
}

.login-card:hover {
  transform: scale(1.02);
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.2);
}

.card-header {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
}

.language-switch {
  position: absolute;
  top: 15px;
  right: 15px;
}

.login-form {
  padding: 20px;
}

.login-input {
  width: 100%;
  transition: all 0.3s ease-in-out;
}

.login-input:hover {
  transform: scale(1.02);
  box-shadow: 0 4px 10px 0 rgba(0, 0, 0, 0.1);
}

.button-form-item {
  display: flex;
  justify-content: center;
}

.login-button {
  width: 100%;
  margin-top: 10px;
  max-width: 200px;
}

.register-link {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 5px;
  margin-top: 20px;
}
</style>