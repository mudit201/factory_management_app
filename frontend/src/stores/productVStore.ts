import Vuex from 'vuex';
Vuex.use(Vuex);
export const productVStore = new Vuex.Store({
  state: {
    products: [],
    loading: false,
    error: null,
    message: null,
  },
  getters: {
    productCount: (state) => state.products.length,
  },
  actions: {
    async loadProducts({ commit }) {
      commit('setLoading', true);
      commit('setError', null);
      try {
        const response = await getAllProducts();
        commit('setProducts', response.data);
      } catch (error) {
        commit('setError', (error as Error).message || "Failed to fetch products");
      } finally {
        commit('setLoading', false);
      }
    },
    async addProduct({ commit }, product) {
      commit('setLoading', true);
      commit('setError', null);
      try {
        const response = await addNewProduct(product);
        if (typeof response.data === "string") {
          commit('setMessage', response.data);
        } else {
          // commit('addProduct', response.data);
          commit('setMessage', "Product added successfully");
        }
      } catch (error) {
        commit('setError', (error as Error).message || "Failed to add product");
      } finally {
        commit('setLoading', false);
      }
    }
  },
  mutations: {
    setProducts(state, products) {
      state.products = products;
    },
    setLoading(state, loading) {
      state.loading = loading;
    },
    setError(state, error) {
      state.error = error;
    },
    setMessage(state, message) {
      state.message = message;
    },
  },
})
