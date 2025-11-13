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
                                <div class="time-filter-item" v-for="timeFilter in timeFilterOptions" :key="timeFilter.value">
                                    <el-checkbox 
                                        v-model="timeFilter.active" 
                                        @change="updateFilter"
                                        class="time-filter-checkbox"
                                    >
                                        {{ timeFilter.label }}
                                    </el-checkbox>
                                    <el-select 
                                        v-if="timeFilter.active"
                                        v-model="timeFilter.order"
                                        placeholder="选择排序方向"
                                        @change="updateFilter"
                                        class="time-order-select"
                                    >
                                        <el-option
                                            label="由近到远"
                                            value="asc"
                                        />
                                        <el-option
                                            label="由远到近"
                                            value="desc"
                                        />
                                    </el-select>
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
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </el-aside>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 时间筛选选项
const timeFilterOptions = ref([
    { label: '截止时间', value: 'dueTime', active: true, order: 'asc' },
    { label: '创建时间', value: 'createdTime', active: false, order: 'asc' },
    { label: '开始时间', value: 'startTime', active: false, order: 'asc' }
])

// 状态筛选
const statusFilter = ref(null) // null: 全部, false: 未完成, true: 已完成

const handleAddTodo = () => {
    router.push('/home/add')
}

// 设置状态筛选
const setStatusFilter = (status) => {
    statusFilter.value = status
    // 触发筛选更新
    updateFilter()
}

// 更新筛选条件（实际应用中会通过事件传递给父组件或其他组件）
const updateFilter = () => {
    // 获取所有激活的时间筛选条件
    const activeFilters = timeFilterOptions.value
        .filter(option => option.active)
        .map(option => ({
            field: option.value,
            order: option.order
        }))
    
    console.log('筛选条件更新:', {
        timeFilters: activeFilters,
        status: statusFilter.value
    })
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

.time-filter-item {
    display: flex;
    flex-direction: column;
    gap: 5px;
    margin-bottom: 10px;
}

.time-filter-item:last-child {
    margin-bottom: 0;
}

.time-filter-checkbox {
    margin-right: 0;
}

.time-order-select {
    margin-left: 20px;
}

.filter-buttons {
    display: flex;
    flex-direction: column;
    gap: 5px;
}

.filter-button {
    justify-content: center;
    font-size: 12px;
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