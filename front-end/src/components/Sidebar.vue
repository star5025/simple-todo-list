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
                        <div class="filter-options">
                            <div class="filter-group">
                                <el-text class="filter-group-label">时间排序</el-text>
                                <div class="filter-buttons">
                                    <el-button 
                                        :type="timeFilter.field === 'dueTime' ? 'primary' : 'default'"
                                        @click="setTimeFilter('dueTime')"
                                        size="small"
                                        class="filter-button"
                                    >
                                        截止时间
                                        <el-icon v-if="timeFilter.field === 'dueTime'">
                                            <ArrowUp v-if="timeFilter.order === 'asc'" />
                                            <ArrowDown v-else />
                                        </el-icon>
                                    </el-button>
                                    <el-button 
                                        :type="timeFilter.field === 'createdTime' ? 'primary' : 'default'"
                                        @click="setTimeFilter('createdTime')"
                                        size="small"
                                        class="filter-button"
                                    >
                                        创建时间
                                        <el-icon v-if="timeFilter.field === 'createdTime'">
                                            <ArrowUp v-if="timeFilter.order === 'asc'" />
                                            <ArrowDown v-else />
                                        </el-icon>
                                    </el-button>
                                    <el-button 
                                        :type="timeFilter.field === 'startTime' ? 'primary' : 'default'"
                                        @click="setTimeFilter('startTime')"
                                        size="small"
                                        class="filter-button"
                                    >
                                        开始时间
                                        <el-icon v-if="timeFilter.field === 'startTime'">
                                            <ArrowUp v-if="timeFilter.order === 'asc'" />
                                            <ArrowDown v-else />
                                        </el-icon>
                                    </el-button>
                                </div>
                            </div>
                            <div class="filter-group">
                                <el-text class="filter-group-label">完成状态</el-text>
                                <div class="filter-buttons">
                                    <el-button 
                                        :type="statusFilter === null ? 'primary' : 'default'"
                                        @click="setStatusFilter(null)"
                                        size="small"
                                        class="filter-button"
                                    >
                                        全部
                                    </el-button>
                                    <el-button 
                                        :type="statusFilter === false ? 'primary' : 'default'"
                                        @click="setStatusFilter(false)"
                                        size="small"
                                        class="filter-button"
                                    >
                                        未完成
                                    </el-button>
                                    <el-button 
                                        :type="statusFilter === true ? 'primary' : 'default'"
                                        @click="setStatusFilter(true)"
                                        size="small"
                                        class="filter-button"
                                    >
                                        已完成
                                    </el-button>
                                </div>
                            </div>
                            <div class="filter-actions">
                                <el-button 
                                    type="primary" 
                                    @click="applyFilters"
                                    style="width: 100%"
                                >
                                    确认筛选
                                </el-button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </el-aside>
</template>

<script setup>
import { ref, inject } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()

// 注入更新筛选条件的方法
const updateFilter = inject('updateFilter', null)

// 时间筛选条件（单选）
const timeFilter = ref({
    field: 'dueTime',  // 默认按截止时间排序
    order: 'asc'       // asc: 从近到远, desc: 从远到近
})

// 状态筛选
const statusFilter = ref(null) // null: 全部, false: 未完成, true: 已完成

const handleAddTodo = () => {
    router.push('/home/add')
}

// 设置时间筛选条件
const setTimeFilter = (field) => {
    if (timeFilter.value.field === field) {
        // 如果点击的是当前激活的字段，则切换排序方向
        timeFilter.value.order = timeFilter.value.order === 'asc' ? 'desc' : 'asc'
    } else {
        // 如果点击的是新的字段，则设置为该字段并默认升序
        timeFilter.value.field = field
        timeFilter.value.order = 'asc'
    }
}

// 设置状态筛选
const setStatusFilter = (status) => {
    statusFilter.value = status
}

// 应用筛选条件
const applyFilters = () => {
    // 构造发送给后端的参数
    const params = {
        page: 1,
        pageSize: 10,
        status: statusFilter.value
    }
    
    // 根据时间字段和排序方向构造orderBy参数
    const fieldMap = {
        'dueTime': 'dueTime',
        'createdTime': 'createdTime',
        'startTime': 'startTime'
    }
    
    const fieldName = fieldMap[timeFilter.value.field] || 'createdTime'
    const orderSuffix = timeFilter.value.order === 'desc' ? 'Desc' : ''
    params.orderBy = fieldName + orderSuffix
    
    // 调用更新筛选条件的方法
    if (updateFilter) {
        updateFilter(params)
    } else {
        console.warn('updateFilter 方法未找到')
    }
    
    console.log('筛选条件已应用:', params)
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

.sidebar-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    /* 使用flex布局让内容自适应 */
    gap: 20px;
    /* 确保内容占满容器高度 */
    min-height: 100%;
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
    display: block;
    margin-bottom: 10px;
}

.filter-options {
    width: 100%;
}

.filter-group {
    margin-bottom: 15px;
}

.filter-group:last-child {
    margin-bottom: 0;
}

.filter-group-label {
    display: block;
    margin-bottom: 5px;
    font-size: 14px;
    color: #666;
}

.filter-buttons {
    display: flex;
    flex-direction: column;
    gap: 5px;
}

.filter-button {
    justify-content: space-between;
    font-size: 12px;
}

.filter-actions {
    padding-top: 10px;
    border-top: 1px solid #eee;
    margin-top: 10px;
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