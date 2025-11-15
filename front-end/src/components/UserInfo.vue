<template>
  <transition name="el-zoom-in-top" appear>
    <div class="user-info-container">
      <el-card class="user-info-card">
        <template #header>
          <div class="card-header">
            <span>个人信息</span>
          </div>
        </template>
        
        <div class="user-info-content" v-loading="loading">
          <div v-if="userInfo" class="user-info-details">
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
            
            <!-- 底部操作按钮 -->
            <div class="user-actions">
              <el-button type="primary" @click="showUsernameDialog">修改用户名</el-button>
              <el-button type="primary" @click="showPasswordDialog">修改密码</el-button>
            </div>
          </div>
          
          <el-empty v-else description="无法加载用户信息" />
        </div>
      </el-card>
    </div>
  </transition>
  
  <!-- 修改用户名对话框 -->
  <el-dialog v-model="usernameDialogVisible" title="修改用户名" width="400px">
    <el-form :model="usernameForm" ref="usernameFormRef">
      <el-form-item label="新用户名" :label-width="80">
        <el-input v-model="usernameForm.newUsername" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="usernameDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateUsername">确认</el-button>
      </span>
    </template>
  </el-dialog>
  
  <!-- 修改密码对话框 -->
  <el-dialog v-model="passwordDialogVisible" title="修改密码" width="400px">
    <el-form :model="passwordForm" ref="passwordFormRef">
      <el-form-item label="原密码" :label-width="80">
        <el-input v-model="passwordForm.oldPassword" type="password" show-password autocomplete="off" />
      </el-form-item>
      <el-form-item label="新密码" :label-width="80">
        <el-input v-model="passwordForm.newPassword" type="password" show-password autocomplete="off" />
      </el-form-item>
      <el-form-item label="确认密码" :label-width="80">
        <el-input v-model="passwordForm.confirmPassword" type="password" show-password autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updatePassword">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

// 用户信息
const userInfo = ref(null)
const loading = ref(false)

// 对话框控制
const usernameDialogVisible = ref(false)
const passwordDialogVisible = ref(false)

// 表单引用
const usernameFormRef = ref(null)
const passwordFormRef = ref(null)

// 用户名表单
const usernameForm = reactive({
  newUsername: ''
})

// 密码表单
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
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

// 显示修改用户名对话框
const showUsernameDialog = () => {
  usernameForm.newUsername = userInfo.value.userName
  usernameDialogVisible.value = true
}

// 显示修改密码对话框
const showPasswordDialog = () => {
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  passwordDialogVisible.value = true
}

// 更新用户名
const updateUsername = async () => {
  if (!usernameForm.newUsername) {
    ElMessage.error('用户名不能为空')
    return
  }
  
  if (usernameForm.newUsername === userInfo.value.userName) {
    ElMessage.info('新用户名不能与当前用户名相同')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要将用户名从 "${userInfo.value.userName}" 修改为 "${usernameForm.newUsername}" 吗？`,
      '确认修改',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    
    // 构造更新数据
    const updateData = {
      userId: userInfo.value.userId,
      userName: usernameForm.newUsername
    }
    
    const response = await request.put('/user', updateData)
    
    if (response.code === 1) {
      ElMessage.success('用户名更新成功')
      usernameDialogVisible.value = false
      // 更新本地存储中的用户名
      localStorage.setItem('userName', usernameForm.newUsername)
      // 发送自定义事件通知Header组件更新用户名
      window.dispatchEvent(new CustomEvent('usernameUpdated', { detail: usernameForm.newUsername }))
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

// 更新密码
const updatePassword = async () => {
  if (!passwordForm.oldPassword || !passwordForm.newPassword) {
    ElMessage.error('密码不能为空')
    return
  }
  
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.error('两次输入的新密码不一致')
    return
  }
  
  if (passwordForm.oldPassword === passwordForm.newPassword) {
    ElMessage.info('新密码不能与原密码相同')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      '确定要修改密码吗？',
      '确认修改',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    
    // 构造更新数据（这里需要同时提供原密码和新密码）
    const updateData = {
      userId: userInfo.value.userId,
      oldPassword: passwordForm.oldPassword,
      userPassword: passwordForm.newPassword
    }
    
    const response = await request.put('/user', updateData)
    
    if (response.code === 1) {
      ElMessage.success('密码更新成功')
      passwordDialogVisible.value = false
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

.user-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>