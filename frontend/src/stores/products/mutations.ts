import type { MutationTree } from "vuex/types/index.js";
import type { ProductsState } from "./state";
import MutationTypes from "./mutation-types";
import type { Product } from "@/types/Product";

export const mutations: MutationTree<ProductsState> = {
  [MutationTypes.SET_ERROR](state, error: string | null) {
    state.error = error;
  },
  [MutationTypes.SET_LOADING](state, loading: boolean) {
    state.loading = loading;
  },
  [MutationTypes.SET_MESSAGE](state, message: string | null) {
    state.message = message;
  },
  [MutationTypes.LOAD_PRODUCTS](state, products: Product[]) {
    state.products = products;
  },
  [MutationTypes.ADD_PRODUCT](state, product: Product) {
    state.products.push(product);
  },
  [MutationTypes.DELETE_PRODUCT](state, productId: string) {
    state.products = state.products.filter(p => p.product !== productId);
  }
}

