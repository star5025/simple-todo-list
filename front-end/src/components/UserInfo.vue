<template>
  <div class="user-info-container">
    <div v-if="!loading">
      <el-card class="user-info-card">
        <template #header>
          <div class="card-header">
            <span>{{ t('userInfo.title') }}</span>
          </div>
        </template>
        
        <div class="user-info-content">
          <div v-if="userInfo" class="user-info-details">
            <div class="info-item">
              <span class="info-label">{{ t('userInfo.username') }}:</span>
              <span class="info-value">{{ userInfo.userName }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">{{ t('userInfo.registrationDate') }}:</span>
              <span class="info-value">{{ formatCreateTime(userInfo.createTime) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">{{ t('userInfo.taskCount') }}:</span>
              <span class="info-value">{{ userInfo.taskCount || 0 }} {{ t('addTodo.unit') || '个' }}</span>
            </div>
            
            <!-- 底部操作按钮 -->
            <div class="user-actions">
              <el-button type="primary" @click="showUsernameDialog">{{ t('userInfo.editUsername') }}</el-button>
              <el-button type="primary" @click="showPasswordDialog">{{ t('userInfo.changePassword') }}</el-button>
            </div>
          </div>
          
          <el-empty v-else :description="t('userInfo.unableToLoadUserInfo')" />
        </div>
      </el-card>
    </div>
    
    <div class="loading-container" v-if="loading">
      <el-skeleton animated>
        <template #template>
          <el-skeleton-item variant="text" style="width: 30%; height: 30px; margin-bottom: 20px" />
          <el-skeleton-item variant="text" style="width: 80%; height: 20px; margin-bottom: 15px" />
          <el-skeleton-item variant="text" style="width: 60%; height: 20px; margin-bottom: 15px" />
          <el-skeleton-item variant="text" style="width: 70%; height: 20px; margin-bottom: 20px" />
          <el-skeleton-item variant="rect" style="width: 200px; height: 40px" />
        </template>
      </el-skeleton>
    </div>
  
  <!-- 修改用户名对话框 -->
  <el-dialog v-model="usernameDialogVisible" :title="t('userInfo.editUsernameTitle')" width="400px" @keydown.enter="handleUsernameEnter">
    <el-form :model="usernameForm" ref="usernameFormRef" @submit.prevent>
      <el-form-item :label="t('userInfo.newUsername')" :label-width="80">
        <el-input v-model="usernameForm.newUsername" autocomplete="off" ref="usernameInput" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="usernameDialogVisible = false">{{ t('userInfo.cancel') }}</el-button>
        <el-button type="primary" @click="updateUsername">{{ t('userInfo.confirm') }}</el-button>
      </span>
    </template>
  </el-dialog>
  
  <!-- 修改密码对话框 -->
  <el-dialog v-model="passwordDialogVisible" :title="t('userInfo.changePasswordTitle')" width="400px" @keydown.enter="handlePasswordEnter">
    <el-form :model="passwordForm" ref="passwordFormRef" @submit.prevent>
      <el-form-item :label="t('userInfo.oldPassword')" :label-width="80">
        <el-input v-model="passwordForm.oldPassword" type="password" show-password autocomplete="off" ref="oldPasswordInput" />
      </el-form-item>
      <el-form-item :label="t('userInfo.newPassword')" :label-width="80">
        <el-input v-model="passwordForm.newPassword" type="password" show-password autocomplete="off" />
      </el-form-item>
      <el-form-item :label="t('userInfo.confirmPassword')" :label-width="80">
        <el-input v-model="passwordForm.confirmPassword" type="password" show-password autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="passwordDialogVisible = false">{{ t('userInfo.cancel') }}</el-button>
        <el-button type="primary" @click="updatePassword">{{ t('userInfo.confirm') }}</el-button>
      </span>
    </template>
  </el-dialog>
</div>
</template>

<script setup>
import { ref, reactive, onMounted, onActivated, onUnmounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
const router = useRouter()

// 用户信息
const userInfo = ref(null)
const loading = ref(false)

// 对话框控制
const usernameDialogVisible = ref(false)
const passwordDialogVisible = ref(false)

// 表单引用
const usernameFormRef = ref(null)
const passwordFormRef = ref(null)
const usernameInput = ref(null)
const oldPasswordInput = ref(null)

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

// ESC键事件处理函数
const handleEscKey = (event) => {
  // 只有在没有打开对话框时才处理ESC键
  if (event.key === 'Escape' && !usernameDialogVisible.value && !passwordDialogVisible.value) {
    router.push('/home/list')
  }
}

// 处理用户名对话框中按下Enter键
const handleUsernameEnter = (event) => {
  event.preventDefault()
  updateUsername()
}

// 处理密码对话框中按下Enter键
const handlePasswordEnter = (event) => {
  event.preventDefault()
  updatePassword()
}

// 组件挂载时添加键盘事件监听
onMounted(() => {
  document.addEventListener('keydown', handleEscKey)
  fetchUserInfo()
})

// 组件卸载时移除键盘事件监听
onUnmounted(() => {
  document.removeEventListener('keydown', handleEscKey)
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
  
  // 聚焦到输入框
  nextTick(() => {
    if (usernameInput.value) {
      usernameInput.value.focus()
    }
  })
}

// 显示修改密码对话框
const showPasswordDialog = () => {
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  passwordDialogVisible.value = true
  
  // 聚焦到输入框
  nextTick(() => {
    if (oldPasswordInput.value) {
      oldPasswordInput.value.focus()
    }
  })
}

// 更新用户名
const updateUsername = async () => {
  if (!usernameForm.newUsername) {
    ElMessage.error(t('userInfo.usernameRequired'))
    return
  }
  
  if (usernameForm.newUsername === userInfo.value.userName) {
    ElMessage.info(t('userInfo.usernameUnchanged'))
    return
  }
  
  try {
    await ElMessageBox.confirm(
      t('userInfo.confirmUsernameChange', [userInfo.value.userName, usernameForm.newUsername]),
      t('userInfo.confirmModification'),
      {
        confirmButtonText: t('userInfo.confirm'),
        cancelButtonText: t('userInfo.cancel'),
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
      ElMessage.success(t('userInfo.usernameUpdated'))
      usernameDialogVisible.value = false
      // 更新本地存储中的用户名
      localStorage.setItem('userName', usernameForm.newUsername)
      // 发送自定义事件通知Header组件更新用户名
      window.dispatchEvent(new CustomEvent('usernameUpdated', { detail: usernameForm.newUsername }))
      // 重新获取用户信息
      fetchUserInfo()
    } else {
      ElMessage.error(response.msg || t('userInfo.updateFailed'))
    }
  } catch (error) {
    // 用户取消操作或请求失败
    if (error !== 'cancel' && error !== 'close') {
      ElMessage.error(t('userInfo.updateFailed'))
    }
  }
}

// 更新密码
const updatePassword = async () => {
  if (!passwordForm.oldPassword || !passwordForm.newPassword) {
    ElMessage.error(t('userInfo.passwordRequired'))
    return
  }
  
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.error(t('userInfo.passwordMismatch'))
    return
  }
  
  if (passwordForm.oldPassword === passwordForm.newPassword) {
    ElMessage.info(t('userInfo.passwordUnchanged'))
    return
  }
  
  try {
    await ElMessageBox.confirm(
      t('userInfo.confirmPasswordChange'),
      t('userInfo.confirmModification'),
      {
        confirmButtonText: t('userInfo.confirm'),
        cancelButtonText: t('userInfo.cancel'),
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
      ElMessage.success(t('userInfo.passwordUpdated'))
      passwordDialogVisible.value = false
    } else {
      ElMessage.error(response.msg || t('userInfo.updateFailed'))
    }
  } catch (error) {
    // 用户取消操作或请求失败
    if (error !== 'cancel' && error !== 'close') {
      ElMessage.error(t('userInfo.updateFailed'))
    }
  }
}

// 组件挂载时获取用户信息
onMounted(() => {
  fetchUserInfo()
})

// 添加onActivated钩子，处理组件被keep-alive缓存后重新激活的情况
onActivated(() => {
  // 组件被激活时重新获取用户信息，确保显示最新数据
  fetchUserInfo()
})
</script>

<style scoped>
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