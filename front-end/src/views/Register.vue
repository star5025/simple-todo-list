<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="card-header">
          <el-text type="primary">
            Simple Todo List
          </el-text>
        </div>
      </template>
      <el-form class="register-form" label-position="left" :label-width="80">
        <el-form-item label="用户名">
          <el-input 
            placeholder="请输入用户名"
            clearable
            class="register-input"
          />
        </el-form-item>
        <el-form-item label="密码">
          <el-input 
            type="password"
            placeholder="请输入密码"
            show-password
            class="register-input"
          />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input 
            type="password"
            placeholder="请再次输入密码"
            show-password
            class="register-input"
          />
        </el-form-item>
        <el-form-item class="button-form-item">
          <el-button 
            type="primary" 
            class="register-button"
          >
            注册
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-link">
        <el-text>已有账号？</el-text>
        <el-button 
          type="primary" 
          link
          @click="goToLogin"
        >
          立即登录
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.register-card {
  width: 100%;
  max-width: 400px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease-in-out;
}

.register-card:hover {
  transform: scale(1.02);
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.2);
}

.card-header {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
}

.register-form {
  padding: 20px;
}

.register-input {
  width: 100%;
  transition: all 0.3s ease-in-out;
}

.register-input:hover {
  transform: scale(1.02);
  box-shadow: 0 4px 10px 0 rgba(0, 0, 0, 0.1);
}

.button-form-item {
  display: flex;
  justify-content: center;
}

.register-button {
  width: 100%;
  margin-top: 10px;
  max-width: 200px;
}

.login-link {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 5px;
  margin-top: 20px;
}
</style>