<!-- 顶栏 -->
<template>
    <el-header class="header">
        <div class="header-container">
            <div class="name-container">
                <el-text type="primary" size="large">Simple Todo List</el-text>
            </div>
            <div class="avatar-container">
                <!-- 这里需要写获取用户头像并展示的逻辑 -->
                <el-avatar>star5025</el-avatar>
            </div>
            <div class="username-container">
                <!-- 这里需要写获取用户名并展示的逻辑 -->
                <el-text>star5025</el-text>
            </div>
            <div class="button-container">
                <el-button type="primary" plain>登出</el-button>
            </div>
        </div>
    </el-header>
</template>

<script setup>

</script>

<style scoped>
.header {
    padding: 10px 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    background-color: white;
}

.header-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-radius: 8px;
    background-color: white;
    box-sizing: border-box;
    width: 100%;
    height: 100%;
}

.name-container {
    flex: 1;
}

.avatar-container, .username-container, .button-container {
    margin-left: 15px;
}
</style>