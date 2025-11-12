<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <el-text type="primary">
            Simple Todo List
          </el-text>
        </div>
      </template>
      <el-form class="login-form" label-position="left" :label-width="80">
        <el-form-item label="用户名">
          <el-input v-model="loginForm.userName" placeholder="请输入用户名" clearable class="login-input" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.userPassword" type="password" placeholder="请输入密码" show-password
            class="login-input" />
        </el-form-item>
        <el-form-item class="button-form-item">
          <el-button type="primary" class="login-button" @click="handleLogin">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="register-link">
        <el-text>还没有账号？</el-text>
        <el-button type="primary" link @click="goToRegister">
          立即注册
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'
import { clearUserInfoCache } from '@/utils/user'

const loginForm = ref({
  userName: '',
  userPassword: ''
})

const router = useRouter()

async function login() {
  // 登录前清除旧的用户信息缓存
  clearUserInfoCache()
  
  // 清除localStorage中的旧用户信息
  localStorage.removeItem('userId')
  localStorage.removeItem('userName')
  
  try {
    const res = await request.post('/user/login', loginForm.value)
    // 注意：现在业务逻辑错误不会进入catch块，而是正常返回
    if (res.code === 1) {
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