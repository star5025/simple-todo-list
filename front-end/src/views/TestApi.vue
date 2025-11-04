// src/views/TestApi.vue
<template>
  <router-view />
  <div>
    <button @click="testApi">测试API</button>
    <div>结果：{{ result }}</div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const result = ref('')

async function testApi() {
  try {
    const res = await fetch('/api/hello') // 注意这里是 /api/hello，不用写完整后端地址
    const responseData = await res.json()
    result.value = responseData.data
  } catch (e) {
    console.log(e)
    result.value = '请求失败'
  }
}
</script>
