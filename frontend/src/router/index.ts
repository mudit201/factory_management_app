import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import BatchView from '@/views/BatchView.vue'
import ProductView from '@/views/ProductView.vue'
import OperatorView from '@/views/OperatorView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/batches',
      name: 'batch',
      component: BatchView,
    },
    {
      path: '/products',
      name: 'product',
      component: ProductView,
    },
    {
      path: '/operators',
      name: 'operators',
      component: OperatorView,
    },
  ],
})

export default router
