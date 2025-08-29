import { shallowMount } from '@vue/test-utils'
import ProductView from '../../../src/views/ProductView.vue'
import { createStore, Store } from 'vuex'

interface Product {
  product: string
  flavor: string
  size: string
  minBatchTime: number
}

interface ProductStoreState {
  loading: boolean
  error: string | null
  products: Product[]
}

describe('ProductView.vue', () => {
  let store: Store<unknown>
  let actions: { [key: string]: jest.Mock }
  let state: ProductStoreState

  beforeEach(() => {
    state = {
      loading: false,
      error: null,
      products: [
        { product: 'Product A', flavor: 'Vanilla', size: 'Large', minBatchTime: 10 },
        { product: 'Product B', flavor: 'Chocolate', size: 'Medium', minBatchTime: 15 },
      ],
    }

    actions = {
      loadProducts: jest.fn(),
      deleteProduct: jest.fn(),
    }

    // store = createStore({
    //   modules: {
    //     productStore: {
    //       namespaced: true,
    //       state,
    //       actions,
    //     },
    //   },
    // });
    store = createStore({
      modules: {
        productStore: {
          namespaced: true,
          state: () => state,
          actions,
          getters: {
            getProductCount: () => state.products.length,
          },
        },
      },
    })
  })

  it('renders product list correctly', () => {
    const wrapper = shallowMount(ProductView, {
      global: {
        plugins: [store],
      },
    })
    expect(wrapper.find('h2').text()).toContain('Products (2)')
    expect(wrapper.findAll('tbody tr').length).toBe(2)
  })

  it('dispatches loadProducts on mount', () => {
    shallowMount(ProductView, {
      global: {
        plugins: [store],
      },
    })
    expect(actions.loadProducts).toHaveBeenCalled()
  })

  it('dispatches deleteProduct when delete icon is clicked', async () => {
    const wrapper = shallowMount(ProductView, {
      global: {
        plugins: [store],
      },
    })
    await wrapper.find('[data-testid="delete-icon"]').trigger('click')
    expect(actions.deleteProduct).toHaveBeenCalledWith(expect.anything(), 'Product A')
  })

  it('shows loading spinner when loading is true', () => {
    state.loading = true

    const store = createStore({
      modules: {
        productStore: {
          namespaced: true,
          state: () => state,
          actions,
          getters: {
            getProductCount: () => state.products.length,
          },
        },
      },
    })

    const wrapper = shallowMount(ProductView, {
      global: {
        plugins: [store],
      },
    })

    expect(wrapper.find('[data-testid="progress-ring"]').exists()).toBe(true)
  })

  it('shows error message when error exists', () => {
    state.error = 'Failed to load products'
    const wrapper = shallowMount(ProductView, {
      global: {
        plugins: [store],
      },
    })
    expect(wrapper.find('.error').text()).toBe('Failed to load products')
  })

  // ...existing code...

  jest.mock('@/stores/bannerStore', () => ({
    useBannerStore: () => ({
      showBannerMessage: jest.fn(),
    }),
  }))

  it('shows "No products found." when products array is empty', () => {
    state.products = []
    const wrapper = shallowMount(ProductView, {
      global: { plugins: [store] },
    })
    expect(wrapper.text()).toContain('No products found.')
  })

  it('toggles modal when Add New Product button is clicked and closed', async () => {
    const wrapper = shallowMount(ProductView, {
      global: { plugins: [store] },
    })

    // Modal should not be visible initially
    expect(wrapper.findComponent({ name: 'ProductFormModal' }).exists()).toBe(false)

    // Click to open modal
    await wrapper.findComponent({ name: 'VButton' }).trigger('click')
    expect(wrapper.findComponent({ name: 'ProductFormModal' }).exists()).toBe(true)

    // Simulate close event from modal
    await wrapper.findComponent({ name: 'ProductFormModal' }).vm.$emit('close')
    await wrapper.vm.$nextTick()
    expect(wrapper.findComponent({ name: 'ProductFormModal' }).exists()).toBe(false)
  })

  // it('shows success banner when product is deleted successfully', async () => {
  //   const { useBannerStore } = require('@/stores/bannerStore');
  //   const showBannerMessage = useBannerStore().showBannerMessage;

  //   actions.deleteProduct.mockResolvedValue();
  //   state.error = null;

  //   const wrapper = shallowMount(ProductView, {
  //     global: { plugins: [store] },
  //   });

  //   await wrapper.find('[data-testid="delete-icon"]').trigger('click');
  //   expect(showBannerMessage).toHaveBeenCalledWith('Product deleted successfully', false);
  // });

  // it('shows error banner when product deletion fails', async () => {
  //   const { useBannerStore } = require('@/stores/bannerStore');
  //   const showBannerMessage = useBannerStore().showBannerMessage;

  //   actions.deleteProduct.mockResolvedValue();
  //   state.error = 'Delete failed';

  //   const wrapper = shallowMount(ProductView, {
  //     global: { plugins: [store] },
  //   });

  //   await wrapper.find('[data-testid="delete-icon"]').trigger('click');
  //   expect(showBannerMessage).toHaveBeenCalledWith('Delete failed', true);
  // });

  // ...existing code...
})
