import { API_BASE_URL } from "@/env-loader";
import axios from "axios";

const api = axios.create({
  // baseURL: import.meta.env.VITE_API_BASE_URL,
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
