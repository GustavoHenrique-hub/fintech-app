// api.js — instância única do axios usada por todos os serviços.
import axios from "axios";
import { TOKEN_KEY } from "@/lib/constants";

const BASE_URL = import.meta.env.VITE_API_URL ?? "/api";

export const api = axios.create({
  baseURL: BASE_URL,
  timeout: 10_000,
  headers: { "Content-Type": "application/json" },
});

// Injeta Bearer token em todas as requisições autenticadas.
api.interceptors.request.use((config) => {
  const token = localStorage.getItem(TOKEN_KEY);
  if (token) config.headers["Authorization"] = `Bearer ${token}`;
  return config;
});

// 401 → dispara evento global para o AuthProvider limpar a sessão e redirecionar.
// 403 → notifica o app (sem permissão para o recurso).
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error?.response?.status;
    if (status === 401) window.dispatchEvent(new CustomEvent("auth:logout"));
    if (status === 403) window.dispatchEvent(new CustomEvent("auth:forbidden"));
    return Promise.reject(error);
  },
);

export const apiUnwrap = async (promise) => (await promise).data;

export default api;
