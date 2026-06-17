// api.js — instância única do axios usada por todos os serviços.
//
// Centraliza:
//   - baseURL configurável via env (VITE_API_URL).
//   - withCredentials para que cookies HttpOnly (JWT / CSRF) sejam enviados.
//   - timeout defensivo (operações financeiras não devem ficar penduradas).
//   - interceptor de request: injeta CSRF token (double-submit cookie).
//   - interceptor de response: trata 401 (sessão expirada) e 403 (sem permissão).
//
// Veja docs/SECURITY.md para o racional de segurança por trás dessas escolhas.
import axios from "axios";

// VITE_* é a única forma do Vite expor env vars para o bundle do navegador.
// Por padrão fica em /api (proxy reverso). Tudo que está aqui é PÚBLICO no
// build final — nunca coloque segredos.
const BASE_URL = import.meta.env.VITE_API_URL ?? "/api";

export const api = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,          // cookies HttpOnly viajam em todas as chamadas
  timeout: 10_000,                // 10s — limites mais altos só em uploads
  headers: { "Content-Type": "application/json" },
});

// ── CSRF: lê o cookie XSRF-TOKEN e ecoa em X-XSRF-TOKEN para PATCH/POST/DELETE.
// O servidor compara header vs cookie; um atacante CSRF não consegue ler o
// cookie de outro domínio → não consegue forjar o header. (Spring Security:
// CookieCsrfTokenRepository.withHttpOnlyFalse()).
function readCookie(name) {
  const match = document.cookie.match(new RegExp(`(?:^|; )${name}=([^;]*)`));
  return match ? decodeURIComponent(match[1]) : null;
}

api.interceptors.request.use((config) => {
  const metodo = (config.method ?? "get").toUpperCase();
  if (["POST", "PATCH", "PUT", "DELETE"].includes(metodo)) {
    const csrf = readCookie("XSRF-TOKEN");
    if (csrf) config.headers["X-XSRF-TOKEN"] = csrf;
  }
  return config;
});

// ── Tratamento global de erros. Cada serviço ainda pode capturar localmente.
let refreshInFlight = null;

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const status = error?.response?.status;
    const original = error.config;

    // 401: sessão expirada → tenta refresh UMA vez. Se já tentou ou falhar,
    // dispara evento que o app escuta para redirecionar ao login.
    if (status === 401 && original && !original._retried) {
      original._retried = true;
      try {
        refreshInFlight = refreshInFlight ?? api.post("/auth/refresh");
        await refreshInFlight;
        refreshInFlight = null;
        return api.request(original);
      } catch {
        refreshInFlight = null;
        window.dispatchEvent(new CustomEvent("auth:logout"));
        return Promise.reject(error);
      }
    }

    // 403: usuário autenticado, mas sem permissão para o recurso.
    if (status === 403) {
      window.dispatchEvent(new CustomEvent("auth:forbidden"));
    }

    // 429: rate limit. Devolve o erro pro chamador respeitar Retry-After.
    return Promise.reject(error);
  },
);

// Atalho conveniente que já desempacota response.data — evita repetir em
// cada service. Quando precisar dos headers/status, use `api` direto.
export const apiUnwrap = async (promise) => (await promise).data;

export default api;
