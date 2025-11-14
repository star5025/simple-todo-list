<!-- 底栏 -->
<template>
  <el-footer class="footer">
    <div class="footer-content">
      <div class="container version-container">
        <el-text type="info" size="small">版本: test</el-text>
      </div>
      
      <div class="container authors-container">
        <el-text type="info" size="small">作者: </el-text>
        <div class="avatar-container">
          <el-avatar 
            src="https://www.github.com/star5025.png" 
            alt="star5025" 
            @click="goToStar5025"
            size="small"
            class="avatar"
          />
        </div>
        
        <div class="avatar-container">
          <el-avatar 
            src="https://www.github.com/Stevan1707.png" 
            alt="Stevan1707" 
            @click="goToStevan1707"
            size="small"
            class="avatar"
          />
        </div>
      </div>
      
      <div class="container github-container" @click="goToRepository">
        <!-- 白色黑底的GitHub图标 -->
        <svg 
          xmlns="http://www.w3.org/2000/svg" 
          width="20" 
          height="20" 
          viewBox="0 0 24 24" 
          fill="#ffffff" 
          stroke="#000000" 
          stroke-width="1" 
          stroke-linecap="round" 
          stroke-linejoin="round" 
          class="github-icon"
        >
          <path d="M9 19c-5 1.5-5-2.5-7-3m14 6v-3.87a3.37 3.37 0 0 0-.94-2.61c3.14-.35 6.44-1.54 6.44-7A5.44 5.44 0 0 0 20 4.77 5.07 5.07 0 0 0 19.91 1S18.73.65 16 2.48a13.38 13.38 0 0 0-7 0C6.27.65 5.09 1 5.09 1A5.07 5.07 0 0 0 5 4.77a5.44 5.44 0 0 0-1.5 3.78c0 5.42 3.3 6.61 6.44 7A3.37 3.37 0 0 0 9 18.13V22"></path>
        </svg>
      </div>
    </div>
  </el-footer>
</template>

<script setup>
const goToStar5025 = () => {
  window.open('https://github.com/star5025', '_blank')
}

const goToStevan1707 = () => {
  window.open('https://github.com/Stevan1707', '_blank')
}

const goToRepository = () => {
  window.open('https://github.com/star5025/simple-todo-list', '_blank')
}
</script>

<style scoped>
.footer {
  background-color: #e2e1e1;
  margin-top: auto;
  width: 100%;
  box-sizing: border-box;
  /* 缩小footer高度 */
  height: 40px;
  min-height: 40px;
  padding: 0;
}

.footer-content {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  padding: 5px 15px;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  border-radius: 6px;
}

.container {
  display: flex;
  align-items: center;
  justify-content: center;
  width: auto;
  max-width: 100%;
  box-sizing: border-box;
}

.version-container {
  margin: 0;
  order: 1;
}

.authors-container {
  order: 2;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.github-container {
  order: 3;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  padding: 2px;
  border-radius: 3px;
  margin: 0;
}

.github-icon:hover {
  transform: scale(1.1);
  transition: all 0.3s ease;
}

.github-icon {
  fill: #333;
  display: block;
  width: 20px;
  height: 20px;
}

.avatar {
  cursor: pointer;
  transition: transform 0.3s ease;
  width: 28px;
  height: 28px;
}

.avatar:hover {
  transform: scale(1.1);
  box-shadow: 0.5px 2px 6px 0px rgba(0, 0, 0, 0.2);
}

/* 如果使用Element Plus，确保el-footer样式正确 */
:global(.el-footer) {
  padding: 0 !important;
}
</style>