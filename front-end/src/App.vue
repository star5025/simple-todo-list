<script setup>
import { useRoute } from 'vue-router'
import Footer from './components/Footer.vue'
import Header from './components/Header.vue';
import Sidebar from './components/Sidebar.vue';

const route = useRoute()
</script>

<template>
  <el-container class="app-container" direction="vertical">
    <Header v-if="route.name !== 'Login' && route.name !== 'Register'" />
    <el-container>
      <Sidebar v-if="route.name !== 'Login' && route.name !== 'Register'" />
      <el-main>
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
}
</style>