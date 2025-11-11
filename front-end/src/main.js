import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 导入Element Plus的容器组件
import { ElContainer, ElMain } from 'element-plus'

import Footer from './components/Footer.vue'
import Header from './components/Header.vue'
import Sidebar from './components/Sidebar.vue'

//封装好的axios
import request from './utils/request'

const app = createApp(App)

//不需要全局挂载
// app.config.globalProperties.$http = request

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

// 注册Element Plus的容器组件
app.component('ElContainer', ElContainer)
app.component('ElMain', ElMain)

app.mount('#app')

// axios.defaults.baseURL = '/api' // 设置axios的基础URL为代理路径