<template>
  <transition name="el-zoom-in-top" appear>
    <div class="user-info-container">
      <el-card class="user-info-card">
        <template #header>
          <div class="card-header">
            <span>{{ isEditing ? '编辑个人信息' : '个人信息' }}</span>
            <div class="header-actions">
              <el-button 
                v-if="!isEditing" 
                @click="startEdit" 
                type="primary" 
                link
              >
                编辑
              </el-button>
            </div>
          </div>
        </template>
        
        <div class="user-info-content" v-loading="loading">
          <div v-if="userInfo && !isEditing" class="user-info-details">
            <div class="info-item">
              <span class="info-label">用户名:</span>
              <span class="info-value">{{ userInfo.userName }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">注册时间:</span>
              <span class="info-value">{{ formatCreateTime(userInfo.createTime) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">创建待办:</span>
              <span class="info-value">{{ userInfo.taskCount || 0 }} 个</span>
            </div>
          </div>
          
          <div v-else-if="userInfo && isEditing" class="user-edit-form">
            <el-form :model="editForm" label-width="100px" ref="editFormRef">
              <el-form-item label="用户名">
                <el-input v-model="editForm.userName" />
              </el-form-item>
              <el-form-item label="新密码">
                <el-input v-model="editForm.userPassword" type="password" show-password />
              </el-form-item>
              <el-form-item label="确认密码">
                <el-input v-model="editForm.confirmPassword" type="password" show-password />
              </el-form-item>
            </el-form>
            
            <div class="form-actions">
              <el-button @click="cancelEdit">取消</el-button>
              <el-button type="primary" @click="confirmEdit">确认修改</el-button>
            </div>
          </div>
          
          <el-empty v-else description="无法加载用户信息" />
        </div>
      </el-card>
    </div>
  </transition>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

// 用户信息
const userInfo = ref(null)
const loading = ref(false)
const isEditing = ref(false)
const editFormRef = ref(null)

// 编辑表单
const editForm = reactive({
  userName: '',
  userPassword: '',
  confirmPassword: ''
})

// 获取用户信息
const fetchUserInfo = async () => {
  loading.value = true
  try {
    // 从localStorage获取用户ID
    const userId = localStorage.getItem('userId')
    
    if (!userId) {
      console.error('未找到用户ID')
      loading.value = false
      return
    }
    
    const response = await request.get(`/user/${userId}`)
    
    if (response.code === 1) {
      userInfo.value = response.data
    } else {
      console.error('获取用户信息失败:', response.msg)
    }
  } catch (error) {
    console.error('获取用户信息时发生错误:', error)
  } finally {
    loading.value = false
  }
}

// 格式化创建时间，只显示年份和日期
const formatCreateTime = (createTime) => {
  if (!createTime) return ''
  const date = new Date(createTime)
  return `${date.getFullYear()}年${String(date.getMonth() + 1).padStart(2, '0')}月${String(date.getDate()).padStart(2, '0')}日`
}

// 开始编辑
const startEdit = () => {
  isEditing.value = true
  // 初始化编辑表单
  if (userInfo.value) {
    editForm.userName = userInfo.value.userName
    editForm.userPassword = ''
    editForm.confirmPassword = ''
  }
}

// 取消编辑
const cancelEdit = () => {
  isEditing.value = false
}

// 确认编辑
const confirmEdit = async () => {
  // 验证密码确认
  if (editForm.userPassword !== editForm.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }
  
  try {
    // 弹出确认对话框
    await ElMessageBox.confirm(
      '确定要修改个人信息吗？',
      '确认修改',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    
    // 构造更新数据
    const updateData = {
      userId: userInfo.value.userId
    }
    
    // 只有当用户名或密码有变化时才添加到更新数据中
    if (editForm.userName !== userInfo.value.userName) {
      updateData.userName = editForm.userName
    }
    
    if (editForm.userPassword) {
      updateData.userPassword = editForm.userPassword
    }
    
    // 如果没有需要更新的字段，则直接返回
    if (!updateData.userName && !updateData.userPassword) {
      ElMessage.info('没有需要更新的信息')
      isEditing.value = false
      return
    }
    
    const response = await request.put('/user', updateData)
    
    if (response.code === 1) {
      ElMessage.success('更新成功')
      isEditing.value = false
      // 更新本地存储中的用户名（如果用户名有变化）
      if (updateData.userName) {
        localStorage.setItem('userName', updateData.userName)
        // 发送自定义事件通知Header组件更新用户名
        window.dispatchEvent(new CustomEvent('usernameUpdated', { detail: updateData.userName }))
      }
      // 重新获取用户信息
      fetchUserInfo()
    } else {
      ElMessage.error(response.msg || '更新失败')
    }
  } catch (error) {
    // 用户取消操作或请求失败
    if (error !== 'cancel' && error !== 'close') {
      ElMessage.error('更新失败')
    }
  }
}

// 组件挂载时获取用户信息
onMounted(() => {
  fetchUserInfo()
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

.user-info-container {
  width: 100%;
}

.user-info-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
}

.user-info-content {
  padding: 20px;
}

.user-info-details {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.info-item {
  display: flex;
  align-items: center;
}

.info-label {
  width: 100px;
  font-weight: bold;
  color: #606266;
}

.info-value {
  margin-left: 20px;
  color: #303133;
}

.user-edit-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>