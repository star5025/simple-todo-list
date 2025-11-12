<!-- 展示待办列表(首页默认展示) -->
<template>
  <div class="todo-list-container">
    <div v-if="groupedTodos.length === 0" class="empty-todos">
      <el-empty description="暂无待办事项" />
    </div>
    
    <div v-else>
      <div v-for="group in groupedTodos" :key="group.date" class="date-group">
        <div class="date-header">
          <el-text class="date-text">{{ group.date }}</el-text>
          <el-text class="count-text">({{ group.todos.length }} 项)</el-text>
        </div>
        
        <el-card 
          v-for="todo in group.todos" 
          :key="todo.id" 
          class="todo-item"
          :class="{ 'overdue': isOverdue(todo.deadline) }"
        >
          <div class="todo-content">
            <div class="todo-info">
              <div class="todo-name">{{ todo.name }}</div>
              <div class="todo-description" v-if="todo.description">
                {{ todo.description }}
              </div>
            </div>
            <div class="todo-deadline">
              <el-tag :type="getDeadlineTagType(todo.deadline)">
                {{ formatDeadline(todo.deadline) }}
              </el-tag>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { format } from 'date-fns'
import { zhCN } from 'date-fns/locale'

// 模拟待办事项数据
const todos = ref([
  {
    id: 1,
    name: '完成项目文档',
    description: '编写并提交项目最终文档',
    deadline: '2025-11-15 18:00',
    completed: false
  },
  {
    id: 2,
    name: '团队会议',
    description: '与团队成员讨论项目进度',
    deadline: '2025-11-13 14:00',
    completed: false
  },
  {
    id: 3,
    name: '学习Vue 3',
    description: '学习Vue 3 Composition API',
    deadline: '2025-11-20 20:00',
    completed: false
  },
  {
    id: 4,
    name: '购买生活用品',
    description: '',
    deadline: '2025-11-12 19:00',
    completed: false
  }
])

// 按日期分组待办事项
const groupedTodos = computed(() => {
  const groups = {}
  
  todos.value.forEach(todo => {
    const date = format(new Date(todo.deadline), 'yyyy年MM月dd日')
    if (!groups[date]) {
      groups[date] = []
    }
    groups[date].push(todo)
  })
  
  // 转换为数组并按日期排序
  return Object.entries(groups)
    .map(([date, todos]) => ({
      date,
      todos: todos.sort((a, b) => new Date(a.deadline) - new Date(b.deadline))
    }))
    .sort((a, b) => new Date(a.todos[0].deadline) - new Date(b.todos[0].deadline))
})

// 格式化截止时间显示
const formatDeadline = (deadline) => {
  const date = new Date(deadline)
  return format(date, 'MM-dd HH:mm')
}

// 获取截止时间标签类型
const getDeadlineTagType = (deadline) => {
  if (isOverdue(deadline)) {
    return 'danger' // 过期红色
  } else if (isDueSoon(deadline)) {
    return 'warning' // 即将到期橙色
  } else {
    return 'success' // 正常绿色
  }
}

// 检查是否过期
const isOverdue = (deadline) => {
  return new Date(deadline) < new Date()
}

// 检查是否即将到期（24小时内）
const isDueSoon = (deadline) => {
  const now = new Date()
  const dueDate = new Date(deadline)
  const diffInHours = (dueDate - now) / (1000 * 60 * 60)
  return diffInHours > 0 && diffInHours <= 24
}
</script>

<style scoped>
.todo-list-container {
  width: 100%;
}

.empty-todos {
  text-align: center;
  padding: 40px 0;
}

.date-group {
  margin-bottom: 30px;
}

.date-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.date-text {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.count-text {
  margin-left: 10px;
  font-size: 14px;
  color: #909399;
}

.todo-item {
  margin-bottom: 15px;
  border-radius: 8px;
  transition: all 0.3s ease-in-out;
}

.todo-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.todo-item.overdue {
  border-left: 4px solid #f56c6c;
}

.todo-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.todo-info {
  flex: 1;
}

.todo-name {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
}

.todo-description {
  font-size: 14px;
  color: #606266;
  line-height: 1.5;
}

.todo-deadline {
  margin-left: 20px;
  flex-shrink: 0;
}
</style>