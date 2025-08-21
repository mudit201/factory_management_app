import { mutations } from '../../../../src/stores/products/mutations';
import { state } from '../../../../src/stores/products/state';

describe('Product Mutations', () => {
  beforeEach(()=>{
    state.loading = false;
    state.error = null;
    state.products = [];
  })

  it('SET_LOADING updates loading state', () => {
    mutations.SET_LOADING(state, true);
    expect(state.loading).toBe(true);
  });

  it('SET_ERROR updates error state', () => {
    mutations.SET_ERROR(state, 'Error message');
    expect(state.error).toBe('Error message');
  });

  it('SET_MESSAGE updates message state', () => {
    mutations.SET_MESSAGE(state, 'Success message');
    expect(state.message).toBe('Success message');
  });

  it('ADD_PRODUCT updates products state', () => {
    const product = { product: 'NP-1', flavor: 'Vanilla', size: '10 g', min_batch_time: 10 };
    mutations.ADD_PRODUCT(state, product);
    expect(state.products).toEqual([product]);
  });

  it('DELETE_PRODUCT deletes product from products state', () => {
    const product = { product: 'NP-1', flavor: 'Vanilla', size: '10 g', min_batch_time: 10 };
    mutations.ADD_PRODUCT(state, product);
    // expect(state.products).toEqual([product]);
    mutations.DELETE_PRODUCT(state, "NP-1");
    expect(state.products).toEqual([]);
  });

});
