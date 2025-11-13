import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import Home from '@/views/Home.vue'
import TodoList from '@/components/TodoList.vue'
import TodoDetail from '@/components/TodoDetail.vue'
import AddTodo from '@/components/AddTodo.vue'
import UserInfo from '@/components/UserInfo.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/home',
      name: 'Home',
      component: Home,
      redirect: '/home/list',
      children: [
        {
          path: 'list',
          name: 'TodoList',
          component: TodoList
        },
        {
          path: 'todo/:id',
          name: 'TodoDetail',
          component: TodoDetail,
          props: true
        },
        {
          path: 'add',
          name: 'AddTodo',
          component: AddTodo
        },
        {
          path: 'userinfo',
          name: 'UserInfo',
          component: UserInfo
        }
      ]
    }
  ]
})

export default router