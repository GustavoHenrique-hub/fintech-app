// GET endpoints do recurso /usuarios.
//
// Backend: UsuarioController (módulo legado — IDs numéricos Long).
// Response: UsuarioResponseDTO.
import { api, apiUnwrap } from "../api";

/** Lista todos os usuários do sistema. */
export function listar() {
  return apiUnwrap(api.get("/usuarios"));
}

/** Busca um usuário pelo id numérico. */
export function buscarPorId(id) {
  return apiUnwrap(api.get(`/usuarios/${id}`));
}
