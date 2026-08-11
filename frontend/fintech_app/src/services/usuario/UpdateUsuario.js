// PATCH /usuarios/{id} — atualizar dados de contato do usuário.
//
// Backend: UsuarioController#atualizar. CPF nunca é aceito aqui — é dado
// sensível e só pode ser alterado via ofício, diretamente no banco de dados.
import { api, apiUnwrap } from "../api";

/**
 * @param {number} id   id numérico do usuário
 * @param {{email?: string, telefone?: string}} dto  campos a atualizar
 */
export function atualizar(id, dto) {
  return apiUnwrap(api.patch(`/usuarios/${id}`, dto));
}

/**
 * PATCH /usuarios/{id}/senha — altera a senha mediante confirmação da atual.
 * @param {number} id
 * @param {{senhaAtual: string, novaSenha: string}} dto
 */
export function alterarSenha(id, dto) {
  return apiUnwrap(api.patch(`/usuarios/${id}/senha`, dto));
}
