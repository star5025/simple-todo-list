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
      <el-form class="register-form" label-position="left" :label-width="80" @keyup.enter="handleRegister">
        <el-form-item label="用户名">
          <el-input 
            v-model="registerForm.userName"
            placeholder="请输入用户名"
            clearable
            class="register-input"
          />
        </el-form-item>
        <el-form-item label="密码">
          <el-input 
            v-model="registerForm.userPassword"
            type="password"
            placeholder="请输入密码"
            show-password
            class="register-input"
          />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input 
            v-model="registerForm.confirmPassword"
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
            @click="handleRegister"
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
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const registerForm = ref({
  userName: '',
  userPassword: '',
  confirmPassword: ''
})

const router = useRouter()

const handleRegister = async () => {
  // 表单验证
  if (!registerForm.value.userName || !registerForm.value.userPassword) {
    ElMessage.error('用户名和密码不能为空')
    return
  }
  
  if (registerForm.value.userPassword !== registerForm.value.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }
  
  try {
    // 只发送用户名和密码到后端，不发送确认密码
    const requestData = {
      userName: registerForm.value.userName,
      userPassword: registerForm.value.userPassword
    }
    
    const res = await request.post('/user/register', requestData)
    // 注意：现在业务逻辑错误不会进入catch块，而是正常返回
    if (res.code === 1) {
      ElMessage.success('注册成功！')
      router.push('/login')
    } else {
      // 只显示后端返回的错误信息，如果没有则显示默认信息
      if (res.msg) {
        ElMessage.error(res.msg)
      } else {
        ElMessage.error('注册失败')
      }
    }
  } catch (error) {
    console.error('注册失败：', error)
    ElMessage.error('网络连接失败，请检查网络')
  }
}

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