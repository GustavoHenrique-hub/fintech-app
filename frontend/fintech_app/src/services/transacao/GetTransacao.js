// GET endpoints do recurso /transacoes.
//
// Backend: TransacaoController (adapters/in/web/transacao).
// Todos retornam TransacaoResponseDTO ou array dele.
import { api, apiUnwrap } from "../api";

/** Lista todas as transações de um usuário (todas as contas). */
export function listarPorUsuario(usuarioId) {
  return apiUnwrap(api.get(`/transacoes/usuario/${usuarioId}`));
}

/** Lista transações vinculadas a uma conta específica. */
export function listarPorConta(contaId) {
  return apiUnwrap(api.get(`/transacoes/conta/${contaId}`));
}

/** Lista os lançamentos criados a partir de um extrato importado. */
export function listarPorExtrato(extratoId) {
  return apiUnwrap(api.get(`/transacoes/extrato/${extratoId}`));
}

/** Busca uma transação pela chave composta (id, code). */
export function buscarPorChave(id, code) {
  return apiUnwrap(api.get(`/transacoes/${id}/${code}`));
}
