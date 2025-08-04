import type { Product } from "@/types/Product";
import api from "./api";


export const getAllProducts = () => api.get<Product[]>("/products");
export const addNewProduct = (product: Product) => api.post<Product | string>("/products", product);
export const deleteProductById = (productId: string) => api.delete(`/products/${productId}`);
