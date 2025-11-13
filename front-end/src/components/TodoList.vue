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
          @click="goToDetail(todo.taskId)"
        >
          <div class="todo-info">
            <el-checkbox 
              v-model="todo.status" 
              @change="updateTodoStatus(todo)"
              @click.stop
              class="todo-checkbox"
            />
            <span 
              class="todo-name" 
              :class="{ 'completed-text': todo.status }"
              >{{ todo.taskName }}</span>
          </div>
          
          <div class="todo-meta">
            <el-tag 
              :type="getCountdownTagType(todo)" 
              size="small"
              class="countdown-tag"
            >
              {{ getCountdownText(todo) }}
            </el-tag>
            <el-icon 
              class="delete-icon" 
              @click.stop="confirmDelete(todo)"
            >
              <Delete />
            </el-icon>
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
import { ref, onMounted, inject, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
import { Delete } from '@element-plus/icons-vue'

const router = useRouter()

// 数据相关
const todos = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 注入筛选条件
const filterParams = inject('filterParams', ref({}))

// 获取待办列表
const fetchTodos = async (filters = {}) => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      ...filters
    }
    
    const response = await request.get('/task/pageQuery', params)
    
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

// 监听筛选条件变化
watch(filterParams, (newParams) => {
  currentPage.value = 1
  fetchTodos(newParams)
}, { deep: true })

// 更新待办状态
const updateTodoStatus = async (todo) => {
  // 如果待办事项已经完成，则不允许再标记为未完成
  if (!todo.status) {
    // 从已完成标记为未完成的情况，直接拒绝
    ElMessage.warning('已完成的待办事项不能标记为未完成')
    // 恢复状态
    todo.status = true
    return
  }
  
  // 待办事项从未完成标记为完成的情况
  try {
    await ElMessageBox.confirm(
      `确定要完成待办事项 "${todo.taskName}" 吗？`,
      '完成确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    );
    
    // 用户确认后执行状态更新
    const response = await request.patch(`/task/${todo.taskId}`, {
      status: todo.status
    })
    
    if (response.code === 1) {
      ElMessage.success('已完成待办')
    } else {
      // 恢复状态
      todo.status = false
      ElMessage.error(response.msg || '更新状态失败')
    }
  } catch (error) {
    // 用户取消操作或请求失败，恢复状态
    if (error !== 'cancel' && error !== 'close') {
      ElMessage.error('更新状态失败')
    }
    todo.status = false
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
  fetchTodos(filterParams.value)
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchTodos(filterParams.value)
}

// 跳转到详情页
const goToDetail = (taskId) => {
  router.push(`/home/todo/${taskId}`)
}

// 确认删除
const confirmDelete = (todo) => {
  ElMessageBox.confirm(
    `确定要删除待办事项 "${todo.taskName}" 吗？此操作无法撤销。`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    deleteTodo(todo)
  }).catch(() => {
    // 用户取消删除
  })
}

// 删除待办项
const deleteTodo = async (todo) => {
  try {
    const response = await request.delete(`/task/${todo.taskId}`)
    
    if (response.code === 1) {
      ElMessage.success('删除成功')
      // 从列表中移除已删除的待办项
      const index = todos.value.findIndex(item => item.taskId === todo.taskId)
      if (index > -1) {
        todos.value.splice(index, 1)
      }
      // 更新总数
      total.value -= 1
    } else {
      ElMessage.error(response.msg || '删除失败')
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

// 暴露方法给父组件
defineExpose({
  fetchTodos
})

// 组件挂载时获取数据
onMounted(() => {
  fetchTodos(filterParams.value)
})
</script>

<style scoped>
.todo-list-container {
  width: 100%;
}

.todo-list-card {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
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

.delete-icon {
  margin-left: 10px;
  cursor: pointer;
  color: #909399;
  font-size: 16px;
}

.delete-icon:hover {
  color: #f56c6c;
}

.countdown-tag {
  margin-left: 10px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 20px 0;
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