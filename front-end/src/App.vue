<script setup>
import Footer from './components/Footer.vue'
import Header from './components/Header.vue';
import Sidebar from './components/Sidebar.vue';
</script>

<template>
  <Header />
  <Sidebar />
  <main>
    <router-view />
  </main>
  <Footer />
</template>

<style>
* {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
}
</style>

<style scoped></style>