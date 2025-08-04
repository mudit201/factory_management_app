import { createStore } from "vuex";
import type { ProductsState } from "./products/state";
import { productStore } from "./products";

export interface RootState {
  productStore: ProductsState;
}

export const store = createStore<RootState>({
  modules: {
    productStore,
  }
});
