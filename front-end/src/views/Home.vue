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
  </el-container>
</template>

<script setup>
import { provide, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import Header from '@/components/Header.vue'
import Sidebar from '@/components/Sidebar.vue'

// 获取当前路由
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
  background-color: #fff;
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
</style>