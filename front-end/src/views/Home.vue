<template>
  <el-container class="home-container">
    <Header />
    
    <el-container class="main-container">
      <Sidebar />
      
      <el-main class="main-content">
        <!-- 主页内容区域 -->
        <div class="home-content">
          <router-view v-slot="{ Component }">
            <transition name="el-fade-in" mode="out-in">
              <component :is="Component" :key="$route.fullPath" v-if="Component" />
            </transition>
          </router-view>
        </div>
      </el-main>
    </el-container>
    
    <!-- 房屋图案按钮，用于跳转到待办项列表 -->
    <div class="home-button" @click="goToTodoList">
      <svg 
        xmlns="http://www.w3.org/2000/svg" 
        width="24" 
        height="24" 
        viewBox="0 0 24 24" 
        fill="none" 
        stroke="currentColor" 
        stroke-width="2" 
        stroke-linecap="round" 
        stroke-linejoin="round"
        class="home-icon"
      >
        <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
        <polyline points="9 22 9 12 15 12 15 22"></polyline>
      </svg>
    </div>
  </el-container>
</template>

<script setup>
import { provide, ref, watch, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElNotification } from 'element-plus'
import Header from '@/components/Header.vue'
import Sidebar from '@/components/Sidebar.vue'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()

// 创建筛选条件响应式引用，设置默认筛选条件
const filterParams = ref({
  status: false,    // 默认只显示未完成的待办事项
  orderBy: 'dueTime' // 默认按截止时间排序
})

// 获取TodoList组件的引用
const todoListRef = ref(null)

// 创建更新筛选条件的方法
const updateFilter = (params) => {
  filterParams.value = {...filterParams.value, ...params}
}

// 提供筛选条件和更新方法给子组件
provide('filterParams', filterParams)
provide('updateFilter', updateFilter)

// 跳转到待办项列表
const goToTodoList = () => {
  router.push('/home/list')
}

// 定时器引用
const reminderTimer = ref(null)

// 检查是否有需要提醒的待办事项
const checkTodaysReminders = async () => {
  try {
    // 获取当前时间
    const now = new Date();
    
    console.log('检查提醒时间:', now);
    
    // 获取所有未完成的待办事项
    const response = await request.get('/task/pageQuery', {
      page: 1,
      pageSize: 100, // 获取足够多的待办事项
      status: false
    });
    
    console.log('获取待办事项响应:', response);
    
    if (response.code === 1) {
      const todos = response.data.records;
      
      console.log('所有待办事项:', todos);
      
      // 筛选出提醒时间已到的待办事项（提醒时间不为空且已到提醒时间）
      const dueReminders = todos.filter(todo => {
        if (todo.remindTime) {
          try {
            const remindDate = new Date(todo.remindTime);
            console.log(`待办事项 "${todo.taskName}" 的提醒时间: ${remindDate}, 当前时间: ${now}`);
            // 判断提醒时间是否已到
            return remindDate <= now;
          } catch (e) {
            console.error('日期解析错误:', e);
            return false;
          }
        }
        return false;
      });
      
      console.log('已到提醒时间的事项:', dueReminders);
      
      // 对每个提醒弹出通知（检查是否已提醒过）
      dueReminders.forEach(todo => {
        // 检查是否已经提醒过这个待办事项（避免重复提醒）
        const reminderKey = `reminder_${todo.taskId}`;
        const isNotified = localStorage.getItem(reminderKey);
        console.log(`提醒键: ${reminderKey}, 是否已提醒: ${isNotified}`);
        
        if (!isNotified) {
          ElNotification({
            title: '待办提醒',
            message: `您有一个待办事项需要处理：${todo.taskName}`,
            type: 'warning',
            duration: 0, // 不自动关闭
            onClick: () => {
              // 点击通知跳转到待办详情页
              router.push(`/home/todo/${todo.taskId}`);
            }
          });
          
          // 记录已提醒，避免重复提醒
          localStorage.setItem(reminderKey, 'notified');
          console.log('已发送提醒通知');
        }
      });
    }
  } catch (error) {
    console.error('检查提醒失败:', error);
  }
};

// 开始定时检查提醒
const startReminderChecks = () => {
  // 立即检查一次
  checkTodaysReminders();
  
  // 每分钟检查一次提醒
  reminderTimer.value = setInterval(() => {
    checkTodaysReminders();
  }, 60000); // 60秒检查一次
};

// 停止定时检查提醒
const stopReminderChecks = () => {
  if (reminderTimer.value) {
    clearInterval(reminderTimer.value);
    reminderTimer.value = null;
  }
};

// 监听路由变化，在路由切换到TodoList时能够正确应用筛选条件
watch(route, (newRoute) => {
  if (newRoute.name === 'TodoList') {
    // 当切换到TodoList页面时，触发一次筛选以确保显示最新数据
    // 这里我们不重置筛选条件，而是保持当前的筛选状态
  }
});

// 组件挂载时检查提醒
onMounted(() => {
  // 延迟一段时间再开始检查提醒，确保页面加载完成
  setTimeout(() => {
    startReminderChecks();
  }, 1000);
});

// 组件卸载时停止检查提醒
onUnmounted(() => {
  stopReminderChecks();
});
</script>

<style scoped>
/* 缩短过渡动画时间 */
.el-fade-in-enter-active {
  transition: opacity 0.2s ease-in-out !important;
}

.el-fade-in-leave-active {
  transition: opacity 0.15s ease-in-out !important;
}

.home-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
}

.main-container {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.main-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: transparent; /* 取消白色背景 */
  display: flex;
  flex-direction: column;
}

.home-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.home-content h1 {
  color: #409EFF;
  margin-bottom: 16px;
}

.home-content p {
  color: #606266;
  font-size: 16px;
}

.home-button {
  position: fixed;
  bottom: 80px; /* 略高于footer */
  right: 30px;
  width: 50px;
  height: 50px;
  background-color: rgba(64, 158, 255, 0.1); /* 低饱和度蓝色背景 */
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  z-index: 1000;
}

.home-button:hover {
  background-color: rgba(64, 158, 255, 0.2); /* 悬停时 slightly increase 饱和度 */
  transform: scale(1.1);
}

.home-icon {
  color: #409EFF;
  width: 24px;
  height: 24px;
}
</style>