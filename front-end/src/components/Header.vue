<!-- 顶栏 -->
<template>
    <el-header class="header">
        <div class="header-container">
            <div class="name-container">
                <el-text type="primary" size="large">Simple Todo List</el-text>
            </div>
            <div class="avatar-container">
                <el-avatar>{{ displayedUserName.charAt(0).toUpperCase() }}</el-avatar>
            </div>
            <div class="username-container">
                <el-text>{{ displayedUserName }}</el-text>
            </div>
            <div class="button-container">
                <el-button type="primary" plain @click="handleLogout">登出</el-button>
            </div>
        </div>
    </el-header>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { clearUserInfoCache } from '@/utils/user'

const router = useRouter()
const displayedUserName = ref('')

// 从localStorage获取用户名
const getUserName = () => {
  return localStorage.getItem('userName') || '未知用户'
}

// 初始化显示的用户名
onMounted(() => {
  displayedUserName.value = getUserName()
  
  // 监听用户名更新事件
  window.addEventListener('usernameUpdated', handleUsernameUpdate)
})

// 处理用户名更新事件
const handleUsernameUpdate = (event) => {
  displayedUserName.value = event.detail
}

const handleLogout = () => {
  // 执行登出逻辑，清除localStorage中的token和userName
  localStorage.removeItem('token')
  localStorage.removeItem('userName')
  localStorage.removeItem('userId')
  // 清除用户信息缓存
  clearUserInfoCache()
  router.push('/login')
}

// 组件销毁时移除事件监听器
onUnmounted(() => {
  window.removeEventListener('usernameUpdated', handleUsernameUpdate)
})
</script>

<style scoped>
.header {
    padding: 10px 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    background-color: white;
    /* 固定header高度为60px */
    height: 60px;
    min-height: 60px;
}

.header-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-radius: 8px;
    background-color: white;
    box-sizing: border-box;
    width: 100%;
    height: 100%;
}

.name-container {
    flex: 1;
}

.avatar-container, .username-container, .button-container {
    margin-left: 15px;
}
</style>