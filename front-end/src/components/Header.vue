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
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const displayedUserName = ref('')

// 从localStorage获取用户名
const getUserName = () => {
  return localStorage.getItem('userName') || '未知用户'
}

// 初始化显示的用户名
onMounted(() => {
  displayedUserName.value = getUserName()
})

const handleLogout = () => {
  // 执行登出逻辑，清除localStorage中的token和userName
  localStorage.removeItem('token')
  localStorage.removeItem('userName')
  router.push('/login')
}
</script>

<style scoped>
.header {
    padding: 10px 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    background-color: white;
    height: 60px; /* 固定header高度 */
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