// import { API_BASE_URL } from "@/env-loader.node";
import axios from "axios";

const api = axios.create({
  // baseURL: import.meta.env.VITE_API_BASE_URL,
  // baseURL: API_BASE_URL,
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
