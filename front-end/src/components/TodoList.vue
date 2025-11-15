<template>
  <transition name="el-zoom-in-top" appear>
    <div class="todo-list-container">
      <el-card class="todo-list-card">
        <template #header>
          <div class="card-header">
            <div class="header-left">
              <el-switch
                v-model="showDateGroups"
                :active-text="showDateGroups ? '显示日期' : '隐藏日期'"
                inactive-text=""
                @change="handleDateGroupToggle"
              />
            </div>
            <div class="header-right">
              <el-button 
                v-if="!batchMode"
                type="danger" 
                :icon="Delete" 
                @click="toggleBatchMode"
              >
                批量删除
              </el-button>
              <el-button 
                v-else
                type="danger" 
                :icon="DeleteFilled" 
                @click="toggleBatchMode"
              >
                退出
              </el-button>
            </div>
          </div>
        </template>
        
        <div v-if="loading" class="loading-container">
          <el-skeleton :rows="5" animated />
        </div>
        
        <div v-else-if="todos.length === 0" class="empty-container">
          <el-empty description="暂无待办事项" />
        </div>
        
        <div v-else>
          <!-- 按日期分组显示待办事项 -->
          <template v-if="showDateGroups">
            <div 
              v-for="(group, date) in groupedTodos" 
              :key="date" 
              class="date-group"
            >
              <div class="date-header">
                <el-text size="large" class="date-title">{{ formatDateHeader(date) }}</el-text>
              </div>
              
              <div 
                v-for="todo in group" 
                :key="todo.taskId" 
                class="todo-item"
                :class="{ 'completed': todo.status, 'selected': batchMode && selectedTodoIds.includes(todo.taskId), 'batch-mode': batchMode }"
                @click="handleTodoClick(todo)"
              >
                <div class="todo-info">
                  <el-checkbox 
                    v-if="!batchMode"
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
                    v-if="!batchMode"
                    class="delete-icon" 
                    @click.stop="confirmDelete(todo)"
                  >
                    <Delete />
                  </el-icon>
                </div>
              </div>
            </div>
          </template>
          
          <!-- 不按日期分组显示待办事项 -->
          <template v-else>
            <div 
              v-for="todo in todos" 
              :key="todo.taskId" 
              class="todo-item"
              :class="{ 'completed': todo.status, 'selected': batchMode && selectedTodoIds.includes(todo.taskId), 'batch-mode': batchMode }"
              @click="handleTodoClick(todo)"
            >
              <div class="todo-info">
                <el-checkbox 
                  v-if="!batchMode"
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
                  v-if="!batchMode"
                  class="delete-icon" 
                  @click.stop="confirmDelete(todo)"
                >
                  <Delete />
                </el-icon>
              </div>
            </div>
          </template>
        </div>
        
        <!-- 批量删除操作栏 -->
        <div v-if="batchMode && selectedTodoIds.length > 0" class="batch-action-bar">
          <div class="batch-info">
            已选择 {{ selectedTodoIds.length }} 项
          </div>
          <div class="batch-actions">
            <el-button type="danger" @click="confirmBatchDelete">删除</el-button>
            <el-button @click="cancelBatchMode">取消</el-button>
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
  </transition>
</template>

<script setup>
import { ref, onMounted, inject, watch, computed, onActivated, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
import { Delete, DeleteFilled } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 数据相关
const todos = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const showDateGroups = ref(true) // 控制是否显示日期分组
const batchMode = ref(false) // 控制是否处于批量删除模式
const selectedTodoIds = ref([]) // 用于批量删除的选中项

// 注入筛选条件
const filterParams = inject('filterParams', ref({}))

// 在组件初始化时，默认添加status=false的筛选条件，只显示未完成的待办事项
const defaultFilterParams = computed(() => {
  return {
    status: false, // 默认只显示未完成的待办事项
    ...filterParams.value
  }
})

// 按指定时间字段分组的待办事项
const groupedTodos = computed(() => {
  const groups = {}
  
  // 确定用于分组的时间字段
  let groupByField = 'createdTime' // 默认按创建时间分组
  
  // 根据当前筛选条件中的orderBy参数决定分组字段
  if (filterParams.value && filterParams.value.orderBy) {
    const orderBy = filterParams.value.orderBy
    if (orderBy.startsWith('dueTime')) {
      groupByField = 'dueTime'
    } else if (orderBy.startsWith('startTime')) {
      groupByField = 'startTime'
    } else if (orderBy.startsWith('createdTime')) {
      groupByField = 'createdTime'
    }
  }
  
  todos.value.forEach(todo => {
    // 获取用于分组的时间字段值
    let timeValue = null
    
    // 根据分组字段获取对应的时间值
    switch (groupByField) {
      case 'dueTime':
        timeValue = todo.dueTime
        break
      case 'startTime':
        timeValue = todo.startTime
        break
      case 'createdTime':
      default:
        // 检查不同的字段名（后端可能使用不同的字段名）
        if (todo.createTime) {
          timeValue = todo.createTime
        } else if (todo.createdTime) {
          timeValue = todo.createdTime
        }
        break
    }
    
    // 获取日期部分（不包含时间）
    let timeStr = null
    if (timeValue) {
      try {
        timeStr = new Date(timeValue).toISOString().split('T')[0]
      } catch (e) {
        console.error('日期解析错误:', e)
      }
    }
    
    if (timeStr) {
      if (!groups[timeStr]) {
        groups[timeStr] = []
      }
      
      groups[timeStr].push(todo)
    } else {
      // 如果没有有效的时间值，归入"未指定"组
      const unspecifiedGroup = '未指定'
      if (!groups[unspecifiedGroup]) {
        groups[unspecifiedGroup] = []
      }
      groups[unspecifiedGroup].push(todo)
    }
  })
  
  return groups
})

// 格式化日期标题
const formatDateHeader = (dateStr) => {
  // 处理"未指定"组
  if (dateStr === '未指定') {
    return '未指定日期'
  }
  
  const date = new Date(dateStr)
  const today = new Date()
  const tomorrow = new Date(today)
  tomorrow.setDate(tomorrow.getDate() + 1)
  
  // 格式化日期为 YYYY-MM-DD
  const formattedDate = date.toISOString().split('T')[0]
  const todayStr = today.toISOString().split('T')[0]
  const tomorrowStr = tomorrow.toISOString().split('T')[0]
  
  if (formattedDate === todayStr) {
    return '今天'
  } else if (formattedDate === tomorrowStr) {
    return '明天'
  } else {
    // 显示为 "11月15日" 格式
    const month = date.getMonth() + 1
    const day = date.getDate()
    return `${month}月${day}日`
  }
}

// 处理日期分组切换
const handleDateGroupToggle = (value) => {
  console.log('日期分组切换:', value)
  // 这里可以添加其他逻辑，如果需要的话
}

// 处理键盘事件
const handleKeydown = (event) => {
  // 只在批量删除模式下处理键盘事件
  if (!batchMode.value) return
  
  // ESC键退出批量删除模式
  if (event.key === 'Escape') {
    cancelBatchMode()
  }
  // Enter键触发确认删除对话框
  else if (event.key === 'Enter' && selectedTodoIds.value.length > 0) {
    event.preventDefault() // 防止表单默认提交行为
    event.stopPropagation() // 阻止事件冒泡
    
    // 检查是否已经有确认对话框打开
    const hasMessageBox = document.querySelector('.el-message-box')
    if (!hasMessageBox) {
      confirmBatchDelete()
    }
  }
}

// 处理批量模式切换
const toggleBatchMode = () => {
  batchMode.value = !batchMode.value
  if (!batchMode.value) {
    // 退出批量模式时清空选中项
    selectedTodoIds.value = []
    // 移除键盘事件监听
    document.removeEventListener('keydown', handleKeydown)
  } else {
    // 进入批量模式时添加键盘事件监听
    document.addEventListener('keydown', handleKeydown)
  }
}

// 处理待办事项点击事件
const handleTodoClick = (todo) => {
  // 如果处于批量模式，则进入选择/取消选择逻辑
  if (batchMode.value) {
    handleTodoSelect(!selectedTodoIds.value.includes(todo.taskId), todo.taskId)
  } else {
    // 否则进入详情页面
    goToDetail(todo.taskId)
  }
}

// 处理待办事项选择事件
const handleTodoSelect = (selected, taskId) => {
  if (selected) {
    // 添加到选中列表
    if (!selectedTodoIds.value.includes(taskId)) {
      selectedTodoIds.value.push(taskId)
    }
  } else {
    // 从选中列表中移除
    const index = selectedTodoIds.value.indexOf(taskId)
    if (index > -1) {
      selectedTodoIds.value.splice(index, 1)
    }
  }
}

// 取消批量模式
const cancelBatchMode = () => {
  batchMode.value = false
  selectedTodoIds.value = []
  // 移除键盘事件监听
  document.removeEventListener('keydown', handleKeydown)
}

// 监听筛选条件变化
watch(filterParams, (newParams) => {
  console.log('筛选条件变化:', newParams)
  currentPage.value = 1
  // 合并默认筛选条件和传入的筛选条件
  const mergedParams = {
    status: false, // 始终保持只显示未完成的待办事项
    ...newParams
  }
  fetchTodos(mergedParams)
}, { deep: true })

// 监听路由变化，确保在返回到待办列表时重新加载数据
watch(
  () => route.name,
  (newRouteName) => {
    if (newRouteName === 'TodoList') {
      // 当路由切换到待办列表时，重新获取数据
      fetchTodos(defaultFilterParams.value)
    }
  }
)

// 添加对路由全路径的监听，确保更可靠的组件更新
watch(
  () => route.fullPath,
  (newPath) => {
    if (newPath.startsWith('/home/list')) {
      // 当路由切换到待办列表时，重新获取数据
      fetchTodos(defaultFilterParams.value)
    }
  }
)

// 使用onActivated钩子处理keep-alive缓存情况
onActivated(() => {
  // 当组件被激活时（从缓存中恢复），重新获取数据
  // 使用nextTick确保在DOM更新后执行
  setTimeout(() => {
    fetchTodos(defaultFilterParams.value)
  }, 0)
})

// 获取待办列表
const fetchTodos = async (filters = {}) => {
  loading.value = true
  try {
    // 确保直接使用 currentPage 变量的值
    const params = {
      page: currentPage.value,  // 直接使用 currentPage 变量
      pageSize: pageSize.value,
      status: false, // 默认只查询未完成的待办事项
      ...filters
    }
    
    console.log('发送请求参数:', params) // 调试信息
    console.log('当前 currentPage 值:', currentPage.value) // 调试信息
    
    const response = await request.get('/task/pageQuery', params)
    
    console.log('后端返回的完整响应:', response) // 调试信息
    
    if (response.code === 1) {
      todos.value = response.data.records
      total.value = response.data.total
      
      // 打印第一条记录来检查字段
      if (response.data.records && response.data.records.length > 0) {
        console.log('第一条待办事项:', response.data.records[0])
      }
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
  // 如果待办事项已完成，直接显示"已完成"
  if (todo.status) {
    return '已完成'
  }
  
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
  // 如果待办事项已完成，显示绿色标签
  if (todo.status) {
    return 'success'
  }
  
  if (!todo.dueTime) return 'info' // 返回'info'类型而不是空字符串
  
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
  console.log('页面大小改变:', val) // 调试信息
  pageSize.value = val
  currentPage.value = 1
  // 合并默认筛选条件
  const mergedParams = {
    status: false, // 始终保持只显示未完成的待办事项
    ...filterParams.value,
    pageSize: val // 确保使用正确的页面大小
  }
  fetchTodos(mergedParams)
}

const handleCurrentChange = (val) => {
  console.log('当前页改变:', val) // 调试信息
  console.log('设置 currentPage 为:', val) // 调试信息
  currentPage.value = val
  // 直接调用 fetchTodos，确保使用更新后的 currentPage 值
  // 合并默认筛选条件
  const mergedParams = {
    status: false, // 始终保持只显示未完成的待办事项
    ...filterParams.value,
    page: val, // 确保使用正确的页码
    pageSize: pageSize.value // 确保使用正确的页面大小
  }
  fetchTodos(mergedParams)
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

// 确认批量删除
const confirmBatchDelete = () => {
  if (selectedTodoIds.value.length === 0) {
    ElMessage.warning('请先选择要删除的待办事项')
    return
  }

  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedTodoIds.value.length} 个待办事项吗？此操作无法撤销。`,
    '批量删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    batchDeleteTodos()
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

// 批量删除待办项
const batchDeleteTodos = async () => {
  if (selectedTodoIds.value.length === 0) {
    ElMessage.warning('请先选择要删除的待办事项')
    return
  }

  try {
    const response = await request.delete('/task/batch/delete', {}, {
      data: selectedTodoIds.value
    })

    if (response.code === 1) {
      ElMessage.success('批量删除成功')
      // 清空选中项
      selectedTodoIds.value = []
      // 退出批量模式
      batchMode.value = false
      // 移除键盘事件监听
      document.removeEventListener('keydown', handleKeydown)
      // 重新获取数据
      fetchTodos(defaultFilterParams.value)
    } else {
      ElMessage.error(response.msg || '批量删除失败')
    }
  } catch (error) {
    ElMessage.error('批量删除失败')
  }
}

// 暴露方法给父组件
defineExpose({
  fetchTodos
})

// 组件卸载时清理事件监听
onUnmounted(() => {
  document.removeEventListener('keydown', handleKeydown)
})

// 组件挂载时获取数据
onMounted(() => {
  fetchTodos(defaultFilterParams.value)
})
</script>

<style scoped>
/* 缩短过渡动画时间 */
.el-zoom-in-top-enter-active {
  transition: all 0.2s cubic-bezier(0.23, 1, 0.32, 1) !important;
}

.el-zoom-in-top-leave-active {
  transition: all 0.15s cubic-bezier(0.755, 0.05, 0.855, 0.06) !important;
}

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

.header-left {
  flex: 1;
}

.header-right {
  font-weight: bold;
  color: #606266;
}

.date-group {
  margin-bottom: 20px;
}

.date-group:last-child {
  margin-bottom: 0;
}

.date-header {
  padding: 10px 0;
  border-bottom: 2px solid #409eff;
  margin-bottom: 10px;
}

.date-title {
  font-weight: bold;
  color: #409eff;
}

.todo-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 10px;
  border-bottom: 1px solid #eee;
  transition: all 0.3s ease;
  position: relative;
}

.todo-item:hover {
  background-color: #f5f7fa;
  transform: scale(1.01);
}

.todo-item:last-child {
  border-bottom: none;
}

.todo-item.selected {
  background-color: #ffffff; /* 使用正常白色背景 */
  border-left: 3px solid #409eff;
}

/* 批量模式下未选中的待办事项使用更明显的灰色调 */
.todo-item:not(.selected).batch-mode {
  background-color: #f0f0f0;
  opacity: 1; /* 移除透明度，使用更明确的背景色 */
}

.todo-info {
  display: flex;
  align-items: center;
  flex: 1;
}

.todo-checkbox {
  margin-right: 12px;
}

.batch-select-checkbox {
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

.batch-action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: #f5f7fa;
  border-top: 1px solid #ebeef5;
  border-bottom: 1px solid #ebeef5;
}

.batch-info {
  font-weight: bold;
  color: #606266;
}

.batch-actions {
  display: flex;
  gap: 10px;
}
</style>