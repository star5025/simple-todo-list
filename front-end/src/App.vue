<script setup>
import Footer from './components/Footer.vue'
</script>

<template>
  <el-container class="app-container" direction="vertical">
    <el-container class="main-container">
      <el-main class="content-main">
        <router-view />
      </el-main>
    </el-container>
    <Footer />
  </el-container>
</template>

<style>
* {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
}

html, body {
  height: 100%;
  margin: 0;
  padding: 0;
}
</style>

<style scoped>
.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-container {
  flex: 1;
  display: flex;
  /* 允许主容器内容滚动 */
  overflow: hidden;
}

.content-main {
  flex: 1;
  overflow-y: auto;
  padding: 0;
}
</style>
