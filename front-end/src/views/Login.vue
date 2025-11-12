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

const loginForm = ref({
  userName: '',
  userPassword: ''
})

const router = useRouter()

async function login() {
  try {
    const res = await request.post('/user/login', loginForm.value)
    // 校验后端响应格式（避免无data字段报错）
    if (!res?.data) throw new Error('登录响应异常')
    console.log(res)
    if (res.data.code === 1) {
      localStorage.setItem('token', res.data.token)
      // ElMessage.success('登录成功！') // 成功提示
      return true // 登录成功返回true
    } else if (res.data.code === 0) {
      // 用后端返回的msg提示，没有则用默认文案
      ElMessage.error(res.data.msg || '登录失败：用户名或密码错误')
      return false // 登录失败返回false
    } else {
      ElMessage.error(res.data.msg || '未知，这又是什么鬼错误啊啊啊啊啊啊')
      return false
    }
  } catch (e) {
    console.error('登录失败：', e)
    return false // 异常时也返回false
  }
}

const loginResult = ref(null)

const handleLogin = async () => {

  loginResult.value = await login()
  // 调用login()，根据返回的状态决定是否跳转
  console.log(loginResult.value)

  // console.log('登录信息:', loginForm.value)

  if (loginResult.value) { // 只有登录成功才执行后续操作
    router.push('/home')
  } else {
    loginForm.userPassword = ''
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