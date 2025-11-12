<template>
  <div class="todo-list-container">
    <el-card class="todo-list-card">
      <template #header>
        <div class="card-header">
          <span>我的待办</span>
        </div>
      </template>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>
      
      <div v-else-if="todos.length === 0" class="empty-container">
        <el-empty description="暂无待办事项" />
      </div>
      
      <div v-else>
        <div 
          v-for="todo in todos" 
          :key="todo.taskId" 
          class="todo-item"
          :class="{ 'completed': todo.status }"
        >
          <div class="todo-info">
            <el-checkbox 
              v-model="todo.status" 
              @change="updateTodoStatus(todo)"
              class="todo-checkbox"
            />
            <span 
              class="todo-name" 
              :class="{ 'completed-text': todo.status }"
            >
              {{ todo.taskName }}
            </span>
          </div>
          
          <div class="todo-meta">
            <el-tag 
              :type="getCountdownTagType(todo)" 
              size="small"
              class="countdown-tag"
            >
              {{ getCountdownText(todo) }}
            </el-tag>
          </div>
        </div>
      </div>
      
      <div v-if="total > 0" class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

// 数据相关
const todos = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 获取待办列表
const fetchTodos = async () => {
  loading.value = true
  try {
    const response = await request.get('/task/pageQuery', {
      page: currentPage.value,
      pageSize: pageSize.value
    })
    
    if (response.code === 1) {
      todos.value = response.data.records
      total.value = response.data.total
    } else {
      ElMessage.error(response.msg || '获取待办列表失败')
    }
  } catch (error) {
    ElMessage.error('获取待办列表失败')
  } finally {
    loading.value = false
  }
}

// 更新待办状态
const updateTodoStatus = async (todo) => {
  try {
    const response = await request.patch(`/task/${todo.taskId}`, {
      status: todo.status
    })
    
    if (response.code === 1) {
      ElMessage.success(todo.status ? '已完成待办' : '已标记为未完成')
    } else {
      // 恢复状态
      todo.status = !todo.status
      ElMessage.error(response.msg || '更新状态失败')
    }
  } catch (error) {
    // 恢复状态
    todo.status = !todo.status
    ElMessage.error('更新状态失败')
  }
}

// 计算倒计时文本
const getCountdownText = (todo) => {
  if (!todo.dueTime) return '无截止时间'
  
  const now = new Date()
  const dueDate = new Date(todo.dueTime)
  const diffTime = dueDate - now
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
  
  if (diffTime < 0) {
    return `已过期 ${Math.abs(diffDays)} 天`
  } else if (diffDays === 0) {
    return '今天到期'
  } else if (diffDays === 1) {
    return '明天到期'
  } else {
    return `${diffDays} 天后到期`
  }
}

// 获取倒计时标签类型
const getCountdownTagType = (todo) => {
  if (!todo.dueTime) return ''
  
  const now = new Date()
  const dueDate = new Date(todo.dueTime)
  const diffTime = dueDate - now
  
  if (diffTime < 0) {
    return 'danger' // 已过期，红色
  } else if (diffTime < 1000 * 60 * 60 * 24) {
    return 'warning' // 一天内到期，橙色
  } else {
    return 'success' // 正常，绿色
  }
}

// 分页相关方法
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  fetchTodos()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchTodos()
}

// 暴露方法给父组件
defineExpose({
  fetchTodos
})

// 组件挂载时获取数据
onMounted(() => {
  fetchTodos()
})
</script>

<style scoped>
.todo-list-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
}

.todo-list-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.todo-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 10px;
  border-bottom: 1px solid #eee;
  transition: all 0.3s ease;
}

.todo-item:hover {
  background-color: #f5f7fa;
  transform: scale(1.01);
}

.todo-item:last-child {
  border-bottom: none;
}

.todo-info {
  display: flex;
  align-items: center;
  flex: 1;
}

.todo-checkbox {
  margin-right: 12px;
}

.todo-name {
  font-size: 16px;
  color: #303133;
}

.completed-text {
  text-decoration: line-through;
  color: #909399 !important;
}

.todo-meta {
  display: flex;
  align-items: center;
}

.countdown-tag {
  margin-left: 10px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 20px 0;
  margin-top: auto;
}

.loading-container,
.empty-container {
  padding: 20px 0;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}

.empty-container {
  width: 100%;
}
</style>