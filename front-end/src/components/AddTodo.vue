<template>
  <div class="add-todo-container">
    <el-card class="add-todo-card">
      <template #header>
        <div class="card-header">
          <span>{{ t('addTodo.title') }}</span>
        </div>
      </template>
      <el-form 
        :model="todoForm" 
        :rules="rules" 
        ref="todoFormRef" 
        label-position="top"
        @submit.prevent="handleSubmit"
        @keyup.enter="handleSubmit"
      >
        <el-form-item :label="t('addTodo.taskName')" prop="taskName">
          <el-input 
            v-model="todoForm.taskName" 
            :placeholder="t('addTodo.enterTaskName')"
            clearable
          />
        </el-form-item>
        
        <el-form-item :label="t('addTodo.description')" prop="description">
          <el-input 
            v-model="todoForm.description" 
            :placeholder="t('addTodo.enterDescription')"
            type="textarea"
            :rows="3"
            clearable
          />
        </el-form-item>
        
        <el-form-item :label="t('addTodo.startTime')" prop="startTime">
          <div class="date-picker-container">
            <el-date-picker
              v-model="todoForm.startTime"
              :type="timePrecision.startTime === 'date' ? 'date' : 'datetime'"
              :placeholder="timePrecision.startTime === 'date' ? t('addTodo.selectStartDate') : t('addTodo.selectStartTime')"
              :format="timePrecision.startTime === 'date' ? 'YYYY-MM-DD' : 'YYYY-MM-DD HH:mm'"
              :value-format="timePrecision.startTime === 'date' ? 'YYYY-MM-DD' : 'YYYY-MM-DD HH:mm'"
              style="width: 100%"
            />
            <el-select 
              v-model="timePrecision.startTime" 
              class="precision-select"
              size="small"
            >
              <el-option :label="t('addTodo.selectStartDate')" value="date"></el-option>
              <el-option :label="t('addTodo.selectStartTime')" value="datetime"></el-option>
            </el-select>
          </div>
        </el-form-item>
        
        <el-form-item :label="t('addTodo.dueTime')" prop="dueTime">
          <div class="date-picker-container">
            <el-date-picker
              v-model="todoForm.dueTime"
              :type="timePrecision.dueTime === 'date' ? 'date' : 'datetime'"
              :placeholder="timePrecision.dueTime === 'date' ? t('addTodo.selectDueDate') : t('addTodo.selectDueTime')"
              :format="timePrecision.dueTime === 'date' ? 'YYYY-MM-DD' : 'YYYY-MM-DD HH:mm'"
              :value-format="timePrecision.dueTime === 'date' ? 'YYYY-MM-DD' : 'YYYY-MM-DD HH:mm'"
              style="width: 100%"
            />
            <el-select 
              v-model="timePrecision.dueTime" 
              class="precision-select"
              size="small"
            >
              <el-option :label="t('addTodo.selectDueDate')" value="date"></el-option>
              <el-option :label="t('addTodo.selectDueTime')" value="datetime"></el-option>
            </el-select>
          </div>
        </el-form-item>
        
        <el-form-item :label="t('addTodo.remindTime')" prop="remindTime">
          <div class="date-picker-container">
            <el-date-picker
              v-model="todoForm.remindTime"
              :type="timePrecision.remindTime === 'date' ? 'date' : 'datetime'"
              :placeholder="timePrecision.remindTime === 'date' ? t('addTodo.selectRemindDate') : t('addTodo.selectRemindTime')"
              :format="timePrecision.remindTime === 'date' ? 'YYYY-MM-DD' : 'YYYY-MM-DD HH:mm'"
              :value-format="timePrecision.remindTime === 'date' ? 'YYYY-MM-DD' : 'YYYY-MM-DD HH:mm'"
              style="width: 100%"
            />
            <el-select 
              v-model="timePrecision.remindTime" 
              class="precision-select"
              size="small"
            >
              <el-option :label="t('addTodo.selectRemindDate')" value="date"></el-option>
              <el-option :label="t('addTodo.selectRemindTime')" value="datetime"></el-option>
            </el-select>
          </div>
        </el-form-item>
        
        <el-form-item :label="t('addTodo.favourite')">
          <el-switch
            v-model="todoForm.favourite"
            :active-text="t('addTodo.favouriteThisTask')"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            @click="handleSubmit"
            :loading="submitting"
            style="width: 100%"
          >
            {{ submitting ? t('addTodo.submitting') : t('addTodo.submit') }}
          </el-button>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            @click="handleCancel"
            style="width: 100%"
          >
            {{ t('addTodo.cancel') }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, watch, onActivated, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useI18n } from 'vue-i18n'
import request from '@/utils/request'

const { t } = useI18n()
const router = useRouter()

const todoFormRef = ref()
const submitting = ref(false)

// 时间精确度控制
const timePrecision = reactive({
  startTime: 'datetime',  // 默认精确到时间
  dueTime: 'datetime',    // 默认精确到时间
  remindTime: 'datetime'  // 默认精确到时间
})

const todoForm = reactive({
  taskName: '',
  description: '',
  startTime: new Date(), // 设置默认值为当前日期和时间
  remindTime: '',
  dueTime: '',
  favourite: false  // 默认不收藏
})

// 监听时间精确度变化
watch(() => timePrecision.startTime, () => {
  if (todoForm.startTime && timePrecision.startTime === 'date') {
    // 如果是从datetime切换到date，只保留日期部分
    const date = new Date(todoForm.startTime);
    todoForm.startTime = date.toISOString().split('T')[0];
  }
})

watch(() => timePrecision.dueTime, () => {
  if (todoForm.dueTime && timePrecision.dueTime === 'date') {
    // 如果是从datetime切换到date，只保留日期部分
    const date = new Date(todoForm.dueTime);
    todoForm.dueTime = date.toISOString().split('T')[0];
  }
})

watch(() => timePrecision.remindTime, () => {
  if (todoForm.remindTime && timePrecision.remindTime === 'date') {
    // 如果是从datetime切换到date，只保留日期部分
    const date = new Date(todoForm.remindTime);
    todoForm.remindTime = date.toISOString().split('T')[0];
  }
})

// ESC键事件处理函数
const handleEscKey = (event) => {
  if (event.key === 'Escape') {
    router.push('/home/list')
  }
}

// 组件挂载时添加键盘事件监听
onMounted(() => {
  document.addEventListener('keydown', handleEscKey)
})

// 组件卸载时移除键盘事件监听
onUnmounted(() => {
  document.removeEventListener('keydown', handleEscKey)
})

const rules = {
  taskName: [
    { required: true, message: t('addTodo.enterTaskName'), trigger: 'blur' },
    { min: 1, max: 50, message: t('addTodo.taskNameLength'), trigger: 'blur' }
  ]
}

const resetForm = () => {
  todoFormRef.value?.resetFields()
}

const handleSubmit = async () => {
  if (!todoFormRef.value) return
  
  await todoFormRef.value.validate(async (valid) => {
    if (valid) {
      // 时间校验逻辑
      // 如果是日期模式，需要特殊处理
      let startTimeObj = null;
      let dueTimeObj = null;
      let remindTimeObj = null;
      
      if (todoForm.startTime) {
        if (timePrecision.startTime === 'date') {
          // 日期模式，创建当天的开始时间
          startTimeObj = new Date(todoForm.startTime);
        } else {
          // 时间模式
          startTimeObj = new Date(todoForm.startTime);
        }
      }
      
      if (todoForm.dueTime) {
        if (timePrecision.dueTime === 'date') {
          // 日期模式，创建当天的结束时间（23:59:59）
          dueTimeObj = new Date(todoForm.dueTime);
          dueTimeObj.setHours(23, 59, 59, 999);
        } else {
          // 时间模式
          dueTimeObj = new Date(todoForm.dueTime);
        }
      }
      
      if (todoForm.remindTime) {
        if (timePrecision.remindTime === 'date') {
          // 日期模式，创建当天的开始时间
          remindTimeObj = new Date(todoForm.remindTime);
        } else {
          // 时间模式
          remindTimeObj = new Date(todoForm.remindTime);
        }
      }
      
      // 校验时间逻辑
      if (startTimeObj && dueTimeObj && startTimeObj > dueTimeObj) {
        ElMessage.error(t('addTodo.startTimeError'))
        return
      }
      
      if (remindTimeObj && dueTimeObj && remindTimeObj > dueTimeObj) {
        ElMessage.error(t('addTodo.remindTimeError'))
        return
      }
      
      // 提交数据到后端
      submitting.value = true
      
      try {
        // 构造请求数据，使用正确的日期格式
        const requestData = {
          taskName: todoForm.taskName,
          description: todoForm.description,
          status: false, // 默认为未完成
          startTime: startTimeObj ? startTimeObj.toISOString() : null,
          remindTime: remindTimeObj ? remindTimeObj.toISOString() : null,
          dueTime: dueTimeObj ? dueTimeObj.toISOString() : null,
          favourite: todoForm.favourite || false // 添加收藏状态
          // userId会由后端自动填充，基于JWT token解析
        }
        
        const res = await request.post('/task/', requestData)
        
        if (res.code === 1) {
          ElMessage.success(t('addTodo.success'))
          // 重置表单
          resetForm()
          // 跳转回待办列表
          router.push('/home/list')
        } else {
          // 只显示后端返回的错误信息，如果没有则显示默认信息
          if (res.msg) {
            ElMessage.error(res.msg)
          } else {
            ElMessage.error(t('addTodo.failure'))
          }
        }
      } catch (error) {
        console.error(t('addTodo.networkError'), error)
        ElMessage.error(t('addTodo.networkError'))
      } finally {
        submitting.value = false
      }
    }
  })
}

// 添加onActivated钩子，处理组件被keep-alive缓存后重新激活的情况
onActivated(() => {
  // 组件被激活时，如果有默认值需要重置，可以在这里处理
  if (!todoForm.startTime) {
    todoForm.startTime = new Date(); // 设置默认值为当前日期和时间
  }
})

const handleCancel = () => {
  resetForm()
  // 跳转回待办列表
  router.push('/home/list')
}

defineExpose({
  resetForm
})
</script>

<style scoped>
.add-todo-container {
  margin-bottom: 20px;
}

.add-todo-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.card-header {
  font-size: 18px;
  font-weight: bold;
  text-align: center;
}

.add-todo-card:hover {
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease-in-out;
}

.date-picker-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.precision-select {
  width: 100px;
}
</style>