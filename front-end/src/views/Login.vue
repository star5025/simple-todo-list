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
          <el-input 
            v-model="loginForm.username"
            placeholder="请输入用户名"
            clearable
            class="login-input"
          />
        </el-form-item>
        <el-form-item label="密码">
          <el-input 
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
            class="login-input"
          />
        </el-form-item>
        <el-form-item class="button-form-item">
          <el-button 
            type="primary" 
            class="login-button"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="register-link">
        <el-text>还没有账号？</el-text>
        <el-button 
          type="primary" 
          link
          @click="goToRegister"
        >
          立即注册
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const loginForm = ref({
  username: '',
  password: ''
})

const router = useRouter()

const handleLogin = () => {
  //进行登录操作

  // 简单的登录逻辑，直接跳转到主页
  console.log('登录信息:', loginForm.value)
  router.push('/home')
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