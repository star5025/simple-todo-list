<script setup>
import { RouterView } from 'vue-router'
import Footer from './components/Footer.vue'
import { onErrorCaptured, ref } from 'vue'
import { ElMessage } from 'element-plus'

const hasError = ref(false)

onErrorCaptured((error) => {
  hasError.value = true
  console.error('全局错误捕获:', error)
  ElMessage.error('页面出现错误，请刷新页面重试')
  return false
})
</script>

<template>
  <el-container class="app-container" direction="vertical">
    <el-container class="main-container" v-if="!hasError">
      <el-main class="content-main">
        <RouterView />
      </el-main>
    </el-container>
    <el-container v-else class="error-container">
      <el-main class="error-main">
        <el-result
          icon="error"
          title="页面出现错误"
          subTitle="请刷新页面重试"
        >
          <template #extra>
            <el-button type="primary" @click="location.reload()">刷新页面</el-button>
          </template>
        </el-result>
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

.error-container {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.error-main {
  width: 100%;
  display: flex;
  justify-content: center;
}
</style>