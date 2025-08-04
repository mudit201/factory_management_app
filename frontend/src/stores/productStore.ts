import { defineStore } from "pinia";
import type { Product } from "@/types/Product";
import { getAllProducts, addNewProduct } from "@/services/productService";

export const useProductStore = defineStore("productStore", {
  state: () => ({
    products: [] as Product[],
    loading: false,
    error: null as string | null,
    message: null as string | null,
  }),
  getters: {
    productCount: (state) => state.products.length,
  },
  actions: {
    async loadProducts() {
      this.loading = true;
      this.error = null;
      try {
        const response = await getAllProducts();
        this.products = response.data;
      } catch (error) {
        this.error = (error as Error).message || "Failed to fetch products";
      } finally {
        this.loading = false;
      }
    },
    async addProduct(product: Product) {
      this.loading = true;
      this.error = null;
      try {
        const response = await addNewProduct(product);
        if (typeof response.data === "string") {
          this.message = response.data;
        } else {
          // this.products.push(response.data);
          this.message = "Product added successfully";
        }
      } catch (error) {
        this.error = (error as Error).message || "Failed to add product";
      } finally {
        this.loading = false;
      }
    }
  },
});
