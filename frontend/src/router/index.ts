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
      name: 'products',
      component: ProductView,
    },
    {
      path: '/operators',
      name: 'operators',
      component: OperatorView,
    },
    {
      path: '/operators/:operator_name',
      name: 'operator_name',
      component: OperatorView,
      props: true,
    },
    {
      path: '/products/new_product',
      name: 'new_product',
      component: ProductView,
    },
  ],
})

export default router
