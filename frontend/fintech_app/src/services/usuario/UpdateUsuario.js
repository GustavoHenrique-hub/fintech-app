// PUT/PATCH /usuarios — atualizar usuário.
//
// NOTA: o UsuarioController atual NÃO expõe um endpoint de atualização
// (apenas POST /, GET / e GET /{id}). Mantemos esta função preparada para
// quando o endpoint for adicionado no backend — provavelmente:
//   PATCH /usuarios/{id}   body: UsuarioRequestDTO parcial
//
// Por enquanto a função joga um Error explícito para que erros de integração
// não passem silenciosamente em desenvolvimento.
import { api, apiUnwrap } from "../api";

/**
 * @param {number} id   id numérico do usuário
 * @param {object} dto  campos a atualizar (UsuarioRequestDTO parcial)
 */
export function atualizar(id, dto) {
  // TODO: descomentar quando o endpoint existir no backend.
  // return apiUnwrap(api.patch(`/usuarios/${id}`, dto));
  void id; void dto;
  throw new Error("Endpoint de atualização de usuário ainda não implementado no backend.");
}
