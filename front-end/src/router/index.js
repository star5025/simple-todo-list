import { createRouter, createWebHistory } from 'vue-router'
import TestApi from '../views/TestApi.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/test-api',
      name: 'test-api',
      component: TestApi
    }
  ]
})

export default router
