import { mount, shallowMount } from '@vue/test-utils';
import ProductFormModal from '../../../src/components/ProductFormModal.vue';
import { createStore, Store } from 'vuex';
import type {ProductsState} from '../../../src/stores/products/state'
import { VTextField, VNumberField, VButton, VProgressRing } from '@vonage/vivid-vue';


describe('ProductFormModal.vue', () => {
  let store: Store<unknown>;
  let actions: { [key: string]: jest.Mock };
  let state: ProductsState;

  beforeEach(() => {
    state = {} as ProductsState;
    actions = {
      addProduct: jest.fn(),
      loadProducts: jest.fn(),
    };
    store = createStore({
      modules: {
        productStore: {
          namespaced: true,
          state: () => state,
          actions,
        },
      },
    });
  });

  it('renders all input fields', () => {
    const wrapper = mount(ProductFormModal, {
      global: { plugins: [store] },
    });
    const textFields = wrapper.findAllComponents(VTextField);
    expect(textFields.length).toBe(3);
    const labels = textFields.map(field => field.props('label'));
    expect(labels).toEqual(['Product code', 'Flavor', 'Size']);
  });

  it('emits close when close button is clicked', async () => {
    const wrapper = shallowMount(ProductFormModal, {
      global: { plugins: [store] },
    });
    await wrapper.find('.close-button').trigger('click');
    expect(wrapper.emitted().close).toBeTruthy();
  });

  it('emits save with product data when save button is clicked', async () => {
    const wrapper = mount(ProductFormModal, {
      global: { plugins: [store] },
    });
    expect(wrapper.findComponent(VButton).exists()).toBe(true);
  });

  it('shows spinner when loading is true', ()=>{
    state.loading = true;
    const wrapper = mount(ProductFormModal, {
      global: { plugins: [store] },
    });

    expect(wrapper.findComponent(VProgressRing).exists()).toBe(true);
    expect(wrapper.findComponent(VButton).exists()).toBe(false);
  });

  it('shows add button and hides spinner when loading is false', () => {
    state = { ...state, loading: false };
    const wrapper = mount(ProductFormModal, {
      global: { plugins: [store] },
    });

    expect(wrapper.findComponent({ name: 'VProgressRing' }).exists()).toBe(false);
    expect(wrapper.findComponent(VButton).exists()).toBe(true);
  });

  it('submits the form with correct data', async () => {
    const wrapper = mount(ProductFormModal, {
      global: { plugins: [store] },
    });

    const [productField, flavorField, sizeField] = wrapper.findAllComponents(VTextField);
    const numberField = wrapper.findComponent(VNumberField);

    await productField.vm.$emit('update:modelValue', 'XY-600');
    await flavorField.vm.$emit('update:modelValue', 'Cola');
    await sizeField.vm.$emit('update:modelValue', '600 ml');
    await numberField.vm.$emit('update:valueAsNumber', 30);

    await wrapper.find('form').trigger('submit.prevent');

    expect(actions.addProduct).toHaveBeenCalledWith(expect.anything(), {
      product: 'XY-600',
      flavor: 'Cola',
      size: '600 ml',
      minBatchTime: 30,
    });
  });

});
