<template>
    <el-aside class="sidebar">
        <div class="sidebar-scroll-container">
            <div class="sidebar-container">
                <div class="add-todo-button-container">
                    <el-button 
                        type="primary" 
                        @click="handleAddTodo"
                        style="width: 100%"
                    >
                        新增待办
                    </el-button>
                </div>
                <div class="selector-container">
                    <div class="selector">
                    <el-text class="selector-label">
                        筛选待办
                    </el-text>
                    <el-select>
                        <!-- 这里涉及vue逻辑，以后完善 -->
                    </el-select>
                </div>
                </div>
                <div class="board-container">
                    <el-card class="upcoming-tasks">
                        <template #header>
                            <div class="upcoming-tasks-header">
                                临近待办
                            </div>
                        </template>
                        <!-- 这里需要写获取临近待办的逻辑 -->
                        <p v-for="o in 4" :key="o" class="text item">{{ 'List item ' + o }}</p>
                    </el-card>
                </div>
                <div class="board-container">
                    <el-card class="starred-tasks">
                        <template #header>
                            <div class="starred-tasks-header">
                                星标待办
                            </div>
                        </template>
                        <!-- 这里需要写获取星标待办的逻辑 -->
                        <p v-for="o in 4" :key="o" class="text item">{{ 'List item ' + o }}</p>
                    </el-card>
                </div>
            </div>
        </div>
    </el-aside>
</template>

<script setup>
// 侧边栏组件 - 移除了新增待办功能

const emit = defineEmits(['add-todo'])

const handleAddTodo = () => {
    emit('add-todo')
}
</script>

<style scoped>
.sidebar {
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    display: flex;
    background-color: rgba(102, 176, 255, 0.228);
    padding: 20px;
    width: 20vw;
    max-width: 300px;
    min-width: 200px;
    /* 侧边栏占满容器高度 */
    height: 100%;
}

.sidebar-scroll-container {
    width: 100%;
    /* 允许侧边栏内容滚动 */
    overflow-y: auto;
    /* 隐藏滚动条但仍可滚动 */
    scrollbar-width: none; /* Firefox */
}

.sidebar-scroll-container::-webkit-scrollbar {
    display: none; /* Chrome, Safari, Opera */
}

.add-todo-button-container {
    width: 100%;
    padding: 10px 0;
}

.selector-container {
    background-color: #fff;
    padding: 10px;
    border-radius: 6px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: all 0.5s ease-in-out;
    width: 100%;
}

.board-container {
    width: 100%;
}

.sidebar-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    /* 使用flex布局让内容自适应 */
    gap: 20px;
    /* 确保内容占满容器高度 */
    min-height: 100%;
}

.board-container:hover {
    scale: 1.02;
    transition: all 0.5s ease-in-out;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.selector-container:hover {
    scale: 1.02;
    background-color: rgb(102, 177, 255);
    transition: all 0.5s ease-in-out;
}

.selector-container:hover .selector-label {
    color: white !important;
}

.selector-label {
    font-size: 16px;
    font-weight: 500;
}

/* 响应式设计 - 针对不同屏幕尺寸调整sidebar宽度 */
@media (max-width: 768px) {
    .sidebar {
        width: 30vw;
        min-width: 150px;
    }
}

@media (max-width: 480px) {
    .sidebar {
        width: 40vw;
        min-width: 120px;
        padding: 10px;
    }
    
    .sidebar-container {
        gap: 15px;
    }
}

@media (min-width: 1200px) {
    .sidebar {
        width: 15vw;
        max-width: 350px;
    }
}
</style>