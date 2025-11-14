<template>
  <el-aside class="sidebar">
    <div class="sidebar-container">
      <!-- 新增待办按钮 -->
      <div class="add-todo-section">
        <button 
          class="custom-button custom-button--primary custom-button--large"
          @click="handleAddTodo"
        >
          <el-icon><Plus /></el-icon>
          新增待办
        </button>
      </div>
      
      <!-- 个人信息按钮 -->
      <div class="user-info-section">
        <button 
          class="custom-button custom-button--primary custom-button--large"
          @click="handleUserInfo"
        >
          个人信息
        </button>
      </div>
      
      <!-- 筛选区域 -->
      <el-card class="filter-card" shadow="never">
        <template #header>
          <div class="filter-header">
            <el-icon><Filter /></el-icon>
            <span>筛选待办</span>
          </div>
        </template>
        
        <div class="filter-content">
          <!-- 时间排序筛选 -->
          <div class="filter-group">
            <el-text class="filter-group-label" size="small">时间排序</el-text>
            <div class="filter-buttons">
              <button 
                :class="[
                  'custom-button', 
                  'custom-button--small',
                  timeFilter.field === 'dueTime' ? 'custom-button--primary' : 'custom-button--default'
                ]"
                @click="setTimeFilter('dueTime')"
              >
                截止时间
                <el-icon v-if="timeFilter.field === 'dueTime'">
                  <ArrowUp v-if="timeFilter.order === 'asc' && timeFilter.field === 'dueTime'" />
                  <ArrowDown v-else-if="timeFilter.order === 'desc' && timeFilter.field === 'dueTime'" />
                </el-icon>
              </button>
              
              <button 
                :class="[
                  'custom-button', 
                  'custom-button--small',
                  timeFilter.field === 'createdTime' ? 'custom-button--primary' : 'custom-button--default'
                ]"
                @click="setTimeFilter('createdTime')"
              >
                创建时间
                <el-icon v-if="timeFilter.field === 'createdTime'">
                  <ArrowUp v-if="timeFilter.order === 'asc' && timeFilter.field === 'createdTime'" />
                  <ArrowDown v-else-if="timeFilter.order === 'desc' && timeFilter.field === 'createdTime'" />
                </el-icon>
              </button>
              
              <button 
                :class="[
                  'custom-button', 
                  'custom-button--small',
                  timeFilter.field === 'startTime' ? 'custom-button--primary' : 'custom-button--default'
                ]"
                @click="setTimeFilter('startTime')"
              >
                开始时间
                <el-icon v-if="timeFilter.field === 'startTime'">
                  <ArrowUp v-if="timeFilter.order === 'asc' && timeFilter.field === 'startTime'" />
                  <ArrowDown v-else-if="timeFilter.order === 'desc' && timeFilter.field === 'startTime'" />
                </el-icon>
              </button>
            </div>
          </div>
          
          <!-- 完成状态筛选 -->
          <div class="filter-group">
            <el-text class="filter-group-label" size="small">完成状态</el-text>
            <div class="filter-buttons">
              <button 
                :class="[
                  'custom-button', 
                  'custom-button--small',
                  statusFilter === null ? 'custom-button--primary' : 'custom-button--default'
                ]"
                @click="setStatusFilter(null)"
              >
                全部
              </button>
              
              <button 
                :class="[
                  'custom-button', 
                  'custom-button--small',
                  statusFilter === false ? 'custom-button--primary' : 'custom-button--default'
                ]"
                @click="setStatusFilter(false)"
              >
                未完成
              </button>
              
              <button 
                :class="[
                  'custom-button', 
                  'custom-button--small',
                  statusFilter === true ? 'custom-button--primary' : 'custom-button--default'
                ]"
                @click="setStatusFilter(true)"
              >
                已完成
              </button>
            </div>
          </div>
          
        </div>
      </el-card>
    </div>
  </el-aside>
</template>

<script setup>
import { ref, inject, watch } from 'vue'
import { useRouter } from 'vue-router'
import { 
  ArrowUp, 
  ArrowDown, 
  Plus,
  Filter 
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()

// 注入更新筛选条件的方法
const updateFilter = inject('updateFilter', null)

// 时间筛选条件（单选）
const timeFilter = ref({
  field: 'dueTime',  // 默认按截止时间排序
  order: 'asc'       // asc: 从近到远, desc: 从远到近
})

// 状态筛选
const statusFilter = ref(null) // null: 全部, false: 未完成, true: 已完成

const handleAddTodo = () => {
  router.push('/home/add')
}

const handleUserInfo = () => {
  router.push('/home/userinfo')
}

// 设置时间筛选条件
const setTimeFilter = (field) => {
  if (timeFilter.value.field === field) {
    // 如果点击的是当前激活的字段
    if (timeFilter.value.order === 'asc') {
      // 第二次点击：切换为降序
      timeFilter.value.order = 'desc'
    } else {
      // 第三次点击：取消选中状态
      timeFilter.value.field = null
      timeFilter.value.order = null
    }
  } else {
    // 如果点击的是新的字段，则设置为该字段并默认升序
    timeFilter.value.field = field
    timeFilter.value.order = 'asc'
  }
  
  // 立即应用筛选条件
  applyFilters()
}

// 设置状态筛选
const setStatusFilter = (status) => {
  statusFilter.value = status
  
  // 立即应用筛选条件
  applyFilters()
}

// 应用筛选条件
const applyFilters = () => {
  // 构造发送给后端的参数
  const params = {
    page: 1,
    pageSize: 10,
    status: statusFilter.value
  }
  
  // 根据时间字段和排序方向构造orderBy参数
  if (timeFilter.value.field && timeFilter.value.order) {
    const fieldMap = {
      'dueTime': 'dueTime',
      'createdTime': 'createdTime',
      'startTime': 'startTime'
    }
    
    const fieldName = fieldMap[timeFilter.value.field] || 'createdTime'
    const orderSuffix = timeFilter.value.order === 'desc' ? 'Desc' : ''
    params.orderBy = fieldName + orderSuffix
  }
  // 如果timeFilter.field或timeFilter.order为null，则不设置orderBy参数
  
  // 调用更新筛选条件的方法
  if (updateFilter) {
    updateFilter(params)
  } else {
    console.warn('updateFilter 方法未找到')
  }
  
  console.log('筛选条件已应用:', params)
}

// 组件挂载时应用一次筛选条件
applyFilters()
</script>

<style scoped>
.sidebar {
  width: 240px;
  min-height: calc(100vh - 140px); /* 增加与header和footer的距离 */
  background-color: #f5f7fa;
  padding: 15px;
  box-sizing: border-box;
  border-right: 1px solid #e4e7ed;
  margin: 10px 0; /* 增加上下边距 */
  border-radius: 8px; /* 添加圆角 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15); /* 添加阴影效果 */
}

.sidebar-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.add-todo-section {
  padding: 8px 0;
}

.user-info-section {
  padding: 8px 0;
}

.filter-card {
  border-radius: 6px;
  border: 1px solid #e4e7ed;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.filter-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.filter-header {
  display: flex;
  align-items: center;
  font-weight: bold;
  color: #606266;
  font-size: 14px;
}

.filter-header .el-icon {
  margin-right: 6px;
  font-size: 14px;
}

.filter-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.filter-group-label {
  font-weight: 500;
  color: #606266;
  font-size: 13px;
}

.filter-buttons {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.status-filter-buttons {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.filter-button {
  width: 100%;
  min-height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  font-size: 12px;
}

/* 自定义按钮样式 */
.custom-button {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  box-sizing: border-box;
  font-size: 13px;
  padding: 6px 12px;
  width: 100%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.custom-button--large {
  padding: 8px 12px;
  font-size: 14px;
  max-width: 100%;
}

.custom-button--small {
  padding: 4px 8px;
  font-size: 12px;
  min-height: 28px;
  width: 100%;
}

.custom-button--primary {
  background-color: #409eff;
  border-color: #409eff;
  color: white;
}

.custom-button--default {
  background-color: #ffffff;
  border-color: #dcdfe6;
  color: #606266;
}

.custom-button:hover {
  opacity: 0.8;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.custom-button--primary:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}

.custom-button--default:hover {
  background-color: #f5f7fa;
  border-color: #c6e2ff;
  color: #409eff;
}
</style>