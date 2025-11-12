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

    <!-- 主要内容区域 -->
    <div class="main-content">
      <AddTodo 
        v-if="showAddTodo" 
        @todo-added="handleTodoAdded" 
        @cancel="handleCancelAdd"
      />
      <TodoList v-else />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AddTodo from '@/components/AddTodo.vue'
import TodoList from '@/components/TodoList.vue'

const props = defineProps({
  showAddTodo: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['todoAdded', 'closeAddTodo'])

const showWelcomeCard = ref(true)

const hideWelcomeCard = () => {
  showWelcomeCard.value = false
}

// 处理新增待办事项事件
const handleTodoAdded = (todo) => {
  // 这里可以处理新增的待办事项，例如更新待办列表
  console.log('新增待办事项:', todo)
  // 通知父组件刷新待办列表
  emit('todoAdded', todo)
  // 关闭添加表单
  emit('closeAddTodo')
}

// 处理取消添加待办事项
const handleCancelAdd = () => {
  emit('closeAddTodo')
}

// 页面加载时默认显示欢迎卡片
onMounted(() => {
  // 每次访问主页时都显示欢迎卡片
  showWelcomeCard.value = true
})
</script>

<style scoped>
.home-container {
  height: 100%;
  position: relative;
  padding: 20px;
  box-sizing: border-box;
  /* 取消内部滚动条 */
  overflow-y: visible;
}

.main-content {
  max-width: 800px;
  margin: 0 auto;
  /* 确保内容区域占满可用空间 */
  min-height: 100%;
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

/* 响应式设计 */
@media (max-width: 768px) {
  .home-container {
    padding: 10px;
  }
  
  .main-content {
    width: 100%;
  }
}
</style>