// GET endpoints do recurso /contas.
//
// Backend: ContaFinanceiraController. Response: ContaFinanceiraResponseDTO.
// Usa enum TipoConta (corrente | poupanca | cartao | dinheiro | investimento).
import { api, apiUnwrap } from "../api";

/** Lista todas as contas financeiras de um usuário. */
export function listarPorUsuario(usuarioId) {
  return apiUnwrap(api.get(`/contas/usuario/${usuarioId}`));
}

/** Busca conta pela chave composta (id, code). */
export function buscarPorChave(id, code) {
  return apiUnwrap(api.get(`/contas/${id}/${code}`));
}
