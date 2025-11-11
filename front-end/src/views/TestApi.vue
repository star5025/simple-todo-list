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
import request from '@/utils/request'

const result = ref('')

async function testApi() {
  try {
    const res = await request.get('/hello1')
    result.value = res.data 
  } catch (e) {
    console.log(e)
    result.value = '请求失败'
  }
}
</script>
