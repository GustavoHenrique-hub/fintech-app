// POST /usuarios — cadastra novo usuário.
//
// Backend: UsuarioController#criar.
// Request: UsuarioRequestDTO  Response: UsuarioResponseDTO (HTTP 201).
import { api, apiUnwrap } from "../api";

/**
 * @param {object} dto UsuarioRequestDTO
 *   { nome, cpf, email, senha, telefone?, dataNascimento? }
 */
export function criar(dto) {
  return apiUnwrap(api.post("/usuarios", dto));
}
