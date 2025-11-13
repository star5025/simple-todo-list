<template>
  <div class="add-todo-container">
    <el-card class="add-todo-card">
      <template #header>
        <div class="card-header">
          <span>新增待办事项</span>
        </div>
      </template>
      <el-form 
        :model="todoForm" 
        :rules="rules" 
        ref="todoFormRef" 
        label-position="top"
        @submit.prevent="handleSubmit"
      >
        <el-form-item label="待办事项名称" prop="taskName">
          <el-input 
            v-model="todoForm.taskName" 
            placeholder="请输入待办事项名称"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="待办事项描述（可选）" prop="description">
          <el-input 
            v-model="todoForm.description" 
            placeholder="请输入待办事项描述"
            type="textarea"
            :rows="3"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="todoForm.startTime"
            type="datetime"
            placeholder="请选择开始时间"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="截止时间" prop="dueTime">
          <el-date-picker
            v-model="todoForm.dueTime"
            type="datetime"
            placeholder="请选择截止时间"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="提醒时间" prop="remindTime">
          <el-date-picker
            v-model="todoForm.remindTime"
            type="datetime"
            placeholder="请选择提醒时间"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            @click="handleSubmit"
            :loading="submitting"
            style="width: 100%"
          >
            {{ submitting ? '添加中...' : '添加待办事项' }}
          </el-button>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            @click="handleCancel"
            style="width: 100%"
          >
            取消
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()

const todoFormRef = ref()
const submitting = ref(false)

const todoForm = reactive({
  taskName: '',
  description: '',
  startTime: '',
  remindTime: '',
  dueTime: ''
})

const rules = {
  taskName: [
    { required: true, message: '请输入待办事项名称', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
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
      if (todoForm.startTime && todoForm.dueTime && new Date(todoForm.startTime) > new Date(todoForm.dueTime)) {
        ElMessage.error('开始时间不能晚于截止时间')
        return
      }
      
      if (todoForm.remindTime && todoForm.dueTime && new Date(todoForm.remindTime) > new Date(todoForm.dueTime)) {
        ElMessage.error('提醒时间不能晚于截止时间')
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
          startTime: todoForm.startTime ? new Date(todoForm.startTime).toISOString() : null,
          remindTime: todoForm.remindTime ? new Date(todoForm.remindTime).toISOString() : null,
          dueTime: todoForm.dueTime ? new Date(todoForm.dueTime).toISOString() : null
          // userId会由后端自动填充，基于JWT token解析
        }
        
        const res = await request.post('/task/', requestData)
        
        if (res.code === 1) {
          ElMessage.success('待办事项添加成功！')
          // 重置表单
          resetForm()
          // 跳转回待办列表
          router.push('/home/list')
        } else {
          // 只显示后端返回的错误信息，如果没有则显示默认信息
          if (res.msg) {
            ElMessage.error(res.msg)
          } else {
            ElMessage.error('添加失败')
          }
        }
      } catch (error) {
        console.error('添加待办事项失败：', error)
        ElMessage.error('网络连接失败，请检查网络')
      } finally {
        submitting.value = false
      }
    }
  })
}

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
</style>