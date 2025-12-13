<!-- 顶栏 -->
<template>
    <el-header class="header">
        <div class="header-container">
            <div class="name-container">
                <el-text type="primary" size="large">Simple Todo List</el-text>
            </div>
            <div class="avatar-container" @click="goToUserInfo">
                <el-avatar class="clickable-avatar">{{ displayedUserName.charAt(0).toUpperCase() }}</el-avatar>
            </div>
            <div class="username-container" @click="goToUserInfo">
                <el-text class="clickable-username">{{ displayedUserName }}</el-text>
            </div>
            <div class="language-selector">
                <el-dropdown @command="switchLanguage">
                    <el-button type="primary" plain>
                        {{ currentLanguage === 'zh-CN' ? '中文' : 'English' }}
                        <el-icon class="el-icon--right">
                            <arrow-down />
                        </el-icon>
                    </el-button>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="zh-CN">中文</el-dropdown-item>
                            <el-dropdown-item command="en-US">English</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
            <div class="button-container">
                <el-button type="primary" plain @click="handleLogout">{{ t('header.logout') }}</el-button>
            </div>
        </div>
    </el-header>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowDown } from '@element-plus/icons-vue'
import { clearUserInfoCache } from '@/utils/user'
import { useI18n } from 'vue-i18n'

const { t, locale } = useI18n()
const router = useRouter()
const displayedUserName = ref('')
const currentLanguage = ref(localStorage.getItem('locale') || 'zh-CN')

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

// 跳转到用户信息页面
const goToUserInfo = () => {
  router.push('/home/userinfo')
}

// 切换语言
const switchLanguage = (lang) => {
  locale.value = lang
  currentLanguage.value = lang
  localStorage.setItem('locale', lang)
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
    padding: 10px 20px !important;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15), 0 2px 4px rgba(0, 0, 0, 0.1) !important;
    background-color: white !important;
    /* 固定header高度为60px */
    height: 60px !important;
    min-height: 60px !important;
    border: none !important;
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

.avatar-container, .username-container, .language-selector, .button-container {
    margin-left: 15px;
}

.clickable-avatar {
    cursor: pointer;
}

.clickable-username {
    cursor: pointer;
}
</style>