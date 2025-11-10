<!-- 底栏 -->
<template>
  <el-footer class="footer">
    <div class="footer-content">
      <div class="container version-container">
        <el-text type="info">版本: test</el-text>
      </div>
      
      <div class="container authors-container">
        <el-text type="info">作者: </el-text>
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
        <svg 
          t="1731251462173" 
          class="github-icon" 
          viewBox="0 0 1024 1024" 
          version="1.1" 
          xmlns="http://www.w3.org/2000/svg" 
          p-id="1560" 
          width="28"
          height="28"
        >
          <path 
            d="M512 71.68c-242.56 0-438.72 196.16-438.72 438.72 0 194.24 126.08 359.04 301.76 417.92 22.08 4.16 30.08-9.6 30.08-21.12 0-10.56-0.32-38.4-0.32-73.28-122.88 26.56-148.8-52.48-148.8-52.48-20.16-51.2-49.28-64.96-49.28-64.96-40.32-27.52 3.2-26.88 3.2-26.88 44.8 3.2 68.16 46.08 68.16 46.08 39.68 67.84 104 48.32 129.6 36.8 4.16-28.8 15.68-48.32 28.48-59.52-99.2-11.2-203.2-49.6-203.2-220.16 0-48.64 17.28-88 45.76-119.36-4.8-11.2-19.84-56 4.16-116.8 0 0 37.12-11.84 121.6 45.76 35.2-9.92 73.28-14.72 111.04-14.72 37.76 0 75.84 4.8 111.04 14.72 84.48-57.6 121.6-45.76 121.6-45.76 24 60.8 9.28 105.6 4.8 116.8 28.8 31.36 45.76 70.72 45.76 119.36 0 171.04-104.32 209.28-203.52 219.84 16 13.76 30.4 41.28 30.4 83.2 0 60.16-0.32 108.8-0.32 123.52 0 11.84 8 25.6 30.4 21.12 175.36-59.2 301.44-224 301.44-418.24C950.72 267.84 754.56 71.68 512 71.68z" 
            p-id="1561">
          </path>
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
  background-color: #f5f5f5;
  border-top: 1px solid #e4e4e4;
  margin-top: auto;
  width: 100%;
  box-sizing: border-box;
  min-height: 120px; /* 确保有足够高度 */
  padding: 0; /* 移除内边距，在内部控制 */
}

.footer-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px 15px;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  gap: 12px; /* 使用gap控制间距 */
}

.container {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  max-width: 100%; /* 防止超出 */
  box-sizing: border-box;
}

.version-container {
  order: 1; /* 控制显示顺序 */
  margin-top: 10px;
  margin-bottom: 5px;
}

.authors-container {
  order: 2;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  flex-wrap: wrap; /* 允许换行 */
}

.github-container {
  order: 3;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  padding: 8px;
  border-radius: 4px;
  margin-top: 5px;
  margin-bottom: 10px;
}

.github-icon:hover {
  transform: scale(1.4);
  transition: all 0.8s ease;
}

.github-icon {
  fill: #333;
  display: block;
}

.avatar {
  cursor: pointer;
  transition: transform 0.8s ease;
}

.avatar:hover {
  transform: scale(1.4);
  box-shadow: 0.5px 2px 8px 0px rgba(0, 0, 0, 0.2);
}

/* 响应式设计 */
/* 暂时不写 */
/* @media (max-width: 480px) {
  .footer-content {
    padding: 15px 10px;
    gap: 10px;
  }
  
  .authors-container {
    gap: 8px;
  }
  
  .github-container {
    padding: 6px;
  }
} */

/* 确保父容器是flex布局 */
:global(body) {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 如果使用Element Plus，确保el-footer样式正确 */
:global(.el-footer) {
  padding: 0 !important;
}
</style>