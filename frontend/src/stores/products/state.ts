import type { Product } from "@/types/Product";

// structure
export interface ProductsState {
  products: Product[];
  loading: boolean;
  error: string | null;
  message: string | null;
}

// initial state
export const state: ProductsState = {
  products: [] as Product[],
  loading: false,
  error: null as string | null,
  message: null as string | null
}
