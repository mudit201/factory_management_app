import type { GetterTree } from "vuex/types/index.js";
import type { ProductsState } from "./state";
import type { RootState } from "..";

export const getters: GetterTree<ProductsState, RootState> = {
  getProductCount(state): number {
    return state.products.length;
  }
}
