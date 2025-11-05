import { createWebHistory, createRouter } from 'vue-router'
import { mount, shallowMount } from '@vue/test-utils'
import ProductView from '../../../src/views/ProductView.vue'
import HomeView from '../../../src/views/HomeView.vue'
import ProductFormModal from '../../../src/components/ProductFormModal.vue'
import App from '../../../src/App.vue'
import { createStore, Store } from 'vuex'
import { createPinia } from 'pinia'

const routes = [
  { path: '/', name: 'home', component: HomeView },
  { path: '/products', name: 'products', component: ProductView },
  { path: '/products/new_product', name: 'new_product', component: ProductView },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

const pinia = createPinia();

describe('router tests', () => {
  let store: Store<unknown>;

  beforeEach(() => {
    store = createStore({
      modules: {
        productStore: {
          namespaced: true,
          state: () => ({
            loading: false,
            error: null,
            products: [
              { product: 'Product A', flavor: 'Vanilla', size: 'Large', minBatchTime: 10 },
              { product: 'Product B', flavor: 'Chocolate', size: 'Medium', minBatchTime: 15 },
            ],
          }),
          actions: {
            loadProducts: jest.fn(),
            deleteProduct: jest.fn(),
          },
          getters: {
            getProductCount: (state) => state.products.length,
          },
        },
      },
    })
  })

  it('tests home route', async () => {
    router.push({ name: 'home' });
    await router.isReady();
    const wrapper = shallowMount(HomeView, {
      global: {
        plugins: [router, pinia],
      },
    });
    expect(router.currentRoute.value.name).toBe('home');
    expect(wrapper.html()).toContain('Dashboard Summary');
  });

  it('renders product view for /products route', async () => {
    router.push({ name: 'products' })
    await router.isReady()
    const wrapper = shallowMount(ProductView, {
      global: {
        plugins: [store, router],
      },
    })
    expect(wrapper.find('h2').text()).toContain('Products (2)')
    expect(wrapper.findAll('tbody tr').length).toBe(2)
  })

  it('render modal for /products/new_product route', async () => {
    router.push({ name: 'new_product' })
    await router.isReady()
    const wrapper = shallowMount(ProductFormModal, {
      global: {
        plugins: [store, router],
      },
    })
    expect(wrapper.find('h2').text()).toContain('Add New Product')
  })

})
