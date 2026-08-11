import { api, apiUnwrap } from "../api";

/**
 * @param {{ email: string, senha: string }} dto
 * @returns {{ token: string, idUsuario: number, usuarioCode: string, expiraEm: string }}
 */
export function login(dto) {
  return apiUnwrap(api.post("/auth/login", dto));
}

/**
 * Invalida o token no servidor.
 * @param {string} token Bearer token atual
 */
export function logout(token) {
  return api.delete("/auth/logout", {
    headers: { Authorization: `Bearer ${token}` },
  });
}
