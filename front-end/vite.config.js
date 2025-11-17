import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    host: '0.0.0.0', // 添加这一行，使Vite监听所有网络接口
    proxy: {
      // 只要前端请求以 /api 开头，就代理到后端
      '/api': {
        target: 'http://backend:8088', // 修改为Docker网络中的后端服务名称
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api/, '/api'), // 保持路径一致
      }
    }
  }
})