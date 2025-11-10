<!-- 顶栏 -->
<template>
    <el-header class="header">
        <div class="header-container">
            <div class="name-container">
                <el-text type="primary" size="large">Simple Todo List</el-text>
            </div>
            <div class="right-container">
                <div class="avatar-container">
                    <!-- 这里需要写获取用户头像并展示的逻辑 -->
                    <el-avatar class="avatar">star5025</el-avatar>
                </div>
                <div class="username-container">
                    <!-- 这里需要写获取用户名并展示的逻辑 -->
                    <el-text>star5025</el-text>
                </div>
                <div class="button-container">
                    <el-button type="primary" plain>登出</el-button>
                </div>
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
    display: flex;
    align-items: center;
}

.header-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px 20px;
    border-radius: 8px;
    background-color: white;
    box-sizing: border-box;
    width: 100%;
}

.name-container {
    font-weight: bold;
}

.right-container {
    display: flex;
    align-items: center;
    gap: 15px;
}

.avatar:hover {
    scale: 1.1;
    transition: 0.5s;
    box-shadow: 0.5px 2px 8px 0px rgba(0, 0, 0, 0.2);
}
</style>
