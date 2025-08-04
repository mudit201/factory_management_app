import type { ActionTree } from "vuex/types/index.js";
import type { ProductsState } from "./state";
import { getAllProducts, addNewProduct, deleteProductById } from "@/services/productService";
import type { RootState } from "..";

export const actions: ActionTree<ProductsState, RootState> = {
  async loadProducts({ commit }) {
    commit("SET_LOADING", true);
    commit("SET_ERROR", null);
    try {
      const response = await getAllProducts();
      commit("LOAD_PRODUCTS", response.data);
    } catch (error) {
      commit("SET_ERROR", (error as Error).message || "Failed to fetch products");
    } finally {
      commit("SET_LOADING", false);
    }
  },
  async addProduct({ commit }, product) {
    commit("SET_LOADING", true);
    commit("SET_ERROR", null);
    try {
      const response = await addNewProduct(product);
      if (typeof response.data === "string") {
        commit("SET_MESSAGE", response.data);
      } else {
        commit("ADD_PRODUCT", response.data);
        commit("SET_MESSAGE", "Product added successfully");
      }
    } catch (error) {
      commit("SET_ERROR", (error as Error).message || "Failed to add product");
    } finally {
      commit("SET_LOADING", false);
    }
  },
  async deleteProduct({ commit }, productId: string) {
    commit("SET_LOADING", true);
    commit("SET_ERROR", null);
    try {
      // Assuming there's a service method to delete a product
      await deleteProductById(productId); // This function should be defined in your service layer
      commit("DELETE_PRODUCT", productId);
      commit("SET_MESSAGE", "Product deleted successfully");
    } catch (error) {
      commit("SET_ERROR", (error as Error).message || "Failed to delete product");
    } finally {
      commit("SET_LOADING", false);
    }
  }
}
