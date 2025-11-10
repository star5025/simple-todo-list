<template>
    <el-aside class="sidebar">
        <div class="sidebar-container">
            <div class="button-container">
                <el-button class="add-button" type="primary">新增待办</el-button>
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
    </el-aside>
</template>

<script setup>

</script>

<style scoped>
.sidebar {
    margin-top: 5px;
    margin-bottom: 5px;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    display: flex;
    background-color: rgba(102, 176, 255, 0.228);
    padding: 20px;
    width: 20vw;
    height: 100vh;
    max-width: 300px;
    min-width: 150px;
    flex-direction: column;
    justify-content: center;
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
    gap: 40px;
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
</style>