<template>
  <el-container class="home-container">
    <Header />
    
    <el-container class="main-container">
      <Sidebar />
      
      <el-main class="main-content">
        <!-- 主页内容区域 -->
        <div class="home-content">
          <router-view />
        </div>
      </el-main>
    </el-container>
    
    <!-- 房屋图案按钮，用于跳转到待办项列表 -->
    <div class="home-button" @click="goToTodoList">
      <svg 
        xmlns="http://www.w3.org/2000/svg" 
        width="24" 
        height="24" 
        viewBox="0 0 24 24" 
        fill="none" 
        stroke="currentColor" 
        stroke-width="2" 
        stroke-linecap="round" 
        stroke-linejoin="round"
        class="home-icon"
      >
        <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
        <polyline points="9 22 9 12 15 12 15 22"></polyline>
      </svg>
    </div>
  </el-container>
</template>

<script setup>
import { provide, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Header from '@/components/Header.vue'
import Sidebar from '@/components/Sidebar.vue'

const router = useRouter()
const route = useRoute()

// 创建筛选条件响应式引用
const filterParams = ref({})

// 创建更新筛选条件的方法
const updateFilter = (params) => {
  filterParams.value = params
}

// 提供筛选条件和更新方法给子组件
provide('filterParams', filterParams)
provide('updateFilter', updateFilter)

// 跳转到待办项列表
const goToTodoList = () => {
  router.push('/home/list')
}

// 监听路由变化，在路由切换到TodoList时能够正确应用筛选条件
watch(route, (newRoute) => {
  if (newRoute.name === 'TodoList') {
    // 当切换到TodoList页面时，触发一次筛选以确保显示最新数据
    // 这里我们不重置筛选条件，而是保持当前的筛选状态
  }
})
</script>

<style scoped>
.home-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
}

.main-container {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.main-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: transparent; /* 取消白色背景 */
  display: flex;
  flex-direction: column;
}

.home-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.home-content h1 {
  color: #409EFF;
  margin-bottom: 16px;
}

.home-content p {
  color: #606266;
  font-size: 16px;
}

.home-button {
  position: fixed;
  bottom: 80px; /* 略高于footer */
  right: 30px;
  width: 50px;
  height: 50px;
  background-color: rgba(64, 158, 255, 0.1); /* 低饱和度蓝色背景 */
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  z-index: 1000;
}

.home-button:hover {
  background-color: rgba(64, 158, 255, 0.2); /* 悬停时 slightly increase 饱和度 */
  transform: scale(1.1);
}

.home-icon {
  color: #409EFF;
  width: 24px;
  height: 24px;
}
</style>