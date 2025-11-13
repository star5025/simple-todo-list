<template>
  <div class="todo-detail-container">
    <el-card class="todo-detail-card">
      <template #header>
        <div class="card-header">
          <el-button @click="goBack" type="primary" link>
            <el-icon><ArrowLeft /></el-icon>
            返回
          </el-button>
          <div class="header-title">待办详情</div>
          <div class="header-actions">
            <el-button @click="editTodo" type="primary" link>
              编辑
            </el-button>
          </div>
        </div>
      </template>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton animated>
          <template #template>
            <el-skeleton-item variant="h3" style="width: 40%" />
            <div style="margin-top: 20px">
              <el-skeleton-item variant="text" style="margin-bottom: 16px" />
              <el-skeleton-item variant="text" style="margin-bottom: 16px" />
              <el-skeleton-item variant="text" style="margin-bottom: 16px" />
              <el-skeleton-item variant="text" style="margin-bottom: 16px" />
            </div>
          </template>
        </el-skeleton>
      </div>
      
      <div v-else-if="todo" class="todo-detail-content">
        <div class="todo-basic-info">
          <div class="todo-status">
            <el-tag :type="todo.status ? 'success' : 'info'">
              {{ todo.status ? '已完成' : '未完成' }}
            </el-tag>
          </div>
          
          <h2 class="todo-title">{{ todo.taskName }}</h2>
          
          <div v-if="todo.description" class="todo-description">
            <h3>描述</h3>
            <p>{{ todo.description }}</p>
          </div>
        </div>
        
        <el-divider />
        
        <div class="todo-time-info">
          <div class="time-item" v-if="todo.createdTime">
            <div class="time-label">创建时间</div>
            <div class="time-value">{{ formatTime(todo.createdTime) }}</div>
          </div>
          
          <div class="time-item" v-if="todo.startTime">
            <div class="time-label">开始时间</div>
            <div class="time-value">{{ formatTime(todo.startTime) }}</div>
          </div>
          
          <div class="time-item" v-if="todo.dueTime">
            <div class="time-label">截止时间</div>
            <div class="time-value">{{ formatTime(todo.dueTime) }}</div>
            <div class="time-remaining">{{ getCountdownText(todo) }}</div>
          </div>
          
          <div class="time-item" v-if="todo.remindTime">
            <div class="time-label">提醒时间</div>
            <div class="time-value">{{ formatTime(todo.remindTime) }}</div>
          </div>
        </div>
      </div>
      
      <div v-else class="empty-container">
        <el-empty description="未找到待办事项" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import { ArrowLeft } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const todo = ref(null)
const loading = ref(false)

// 获取待办详情
const fetchTodoDetail = async () => {
  const taskId = route.params.id
  if (!taskId) {
    ElMessage.error('参数错误')
    return
  }
  
  loading.value = true
  try {
    const response = await request.get(`/task/${taskId}`)
    if (response.code === 1) {
      todo.value = response.data
    } else {
      ElMessage.error(response.msg || '获取待办详情失败')
    }
  } catch (error) {
    ElMessage.error('获取待办详情失败')
  } finally {
    loading.value = false
  }
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

// 计算倒计时文本
const getCountdownText = (todo) => {
  if (!todo.dueTime) return ''
  
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

// 返回上一页
const goBack = () => {
  router.push('/home')
}

// 编辑待办
const editTodo = () => {
  ElMessage.info('编辑功能待实现')
}

// 组件挂载时获取数据
onMounted(() => {
  fetchTodoDetail()
})
</script>

<style scoped>
.todo-detail-container {
  width: 100%;
}

.todo-detail-card {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
}

.todo-detail-content {
  padding: 20px 0;
}

.todo-basic-info {
  margin-bottom: 20px;
}

.todo-status {
  margin-bottom: 15px;
}

.todo-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #303133;
}

.todo-description h3 {
  margin-top: 0;
  margin-bottom: 10px;
  color: #606266;
}

.todo-description p {
  margin: 0;
  color: #606266;
  line-height: 1.6;
}

.todo-time-info {
  padding: 0 20px;
}

.time-item {
  display: flex;
  margin-bottom: 15px;
  align-items: center;
}

.time-label {
  width: 80px;
  color: #909399;
  font-size: 14px;
}

.time-value {
  flex: 1;
  color: #303133;
  margin-left: 20px;
}

.time-remaining {
  margin-left: 10px;
  font-size: 12px;
  color: #909399;
}

.loading-container,
.empty-container {
  padding: 50px 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.empty-container {
  width: 100%;
}
</style>