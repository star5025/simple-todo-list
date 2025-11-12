<script setup>
import { useRoute } from 'vue-router'
import { ref } from 'vue'
import Footer from './components/Footer.vue'
import Header from './components/Header.vue';
import Sidebar from './components/Sidebar.vue';

const route = useRoute()
const showAddTodo = ref(false)

const handleAddTodo = () => {
  showAddTodo.value = true
}

const handleTodoAdded = () => {
  showAddTodo.value = false
}

const handleCloseAddTodo = () => {
  showAddTodo.value = false
}
</script>

<template>
  <el-container class="app-container" direction="vertical">
    <Header v-if="route.name !== 'Login' && route.name !== 'Register'" />
    <el-container class="main-container">
      <Sidebar 
        v-if="route.name !== 'Login' && route.name !== 'Register'" 
        @add-todo="handleAddTodo"
      />
      <el-main class="content-main">
        <router-view 
          :show-add-todo="showAddTodo" 
          @todo-added="handleTodoAdded"
          @close-add-todo="handleCloseAddTodo"
        />
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