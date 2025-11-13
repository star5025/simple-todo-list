<template>
  <el-container class="home-container">
    <Header />
    
    <el-container class="main-container">
      <Sidebar @add-todo="showAddTodo" />
      
      <el-main class="main-content">
        <!-- 主页内容区域 -->
        <div class="home-content">
          <AddTodo v-if="showAddTodoForm" @todo-added="handleTodoAdded" />
          <TodoList v-else ref="todoListRef" />
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from 'vue'
import Header from '@/components/Header.vue'
import Sidebar from '@/components/Sidebar.vue'
import AddTodo from '@/components/AddTodo.vue'
import TodoList from '@/components/TodoList.vue'

const showAddTodoForm = ref(false)
const todoListRef = ref(null)

const showAddTodo = () => {
  showAddTodoForm.value = true
}

// 当新增待办完成后，可以调用此方法刷新列表并返回列表视图
const handleTodoAdded = () => {
  showAddTodoForm.value = false
  // 刷新待办列表
  if (todoListRef.value) {
    todoListRef.value.fetchTodos()
  }
}

// 将handleTodoAdded方法提供给AddTodo组件使用
defineExpose({
  handleTodoAdded
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