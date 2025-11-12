<template>
  <div class="home-container">
    <!-- 首次进入时的弹出卡片 -->
    <div v-if="showWelcomeCard" class="overlay" @click="hideWelcomeCard">
      <el-card class="welcome-card" @click.stop>
        <div class="content">
          <h1>欢迎来到 Simple Todo List 🥳</h1>
          <p>这是一个简单的待办事项管理应用</p>
          <el-button type="primary" @click="hideWelcomeCard">开始使用</el-button>
        </div>
      </el-card>
    </div>

    <!-- 常规主页内容 -->
    <el-card class="home-card">
      <template #header>
        <div class="card-header">
          <el-text type="primary">
            Simple Todo List
          </el-text>
        </div>
      </template>
      <div class="content">
        <h1>欢迎来到 Simple Todo List</h1>
        <p>这是一个简单的待办事项管理应用</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted} from 'vue'

const showWelcomeCard = ref(true)

const hideWelcomeCard = () => {
  showWelcomeCard.value = false
}

// 页面加载时默认显示欢迎卡片
onMounted(() => {
  // 每次访问主页时都显示欢迎卡片
  showWelcomeCard.value = true
})
</script>

<style scoped>
.home-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  min-height: 400px;
  position: relative;
}

.home-card {
  width: 100%;
  max-width: 400px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease-in-out;
}

.home-card:hover {
  transform: scale(1.02);
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.2);
}

/* 弹出卡片的遮罩层 */
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease-out;
}

/* 弹出卡片样式 */
.welcome-card {
  width: 100%;
  max-width: 400px;
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease-in-out;
  animation: slideIn 0.3s ease-out;
}

.welcome-card:hover {
  transform: scale(1.02);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.4);
}

.card-header {
  text-align: center;
  font-size: 20px;
  font-weight: bold;
}

.content {
  padding: 30px;
  text-align: center;
}

.content h1 {
  margin-bottom: 20px;
  color: #333;
  font-size: 24px;
}

.content p {
  margin-bottom: 30px;
  color: #666;
  font-size: 16px;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideIn {
  from {
    transform: translateY(-20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}
</style>