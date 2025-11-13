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
import Header from '@/components/Header.vue'
import Sidebar from '@/components/Sidebar.vue'
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