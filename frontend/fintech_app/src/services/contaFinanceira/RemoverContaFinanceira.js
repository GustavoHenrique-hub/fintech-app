// PATCH /contas/{id}/{code}/remover — soft delete da conta financeira.
//
// Backend: ContaFinanceiraController#remover. Marca indDelete='S' e ativa=false;
// não remove fisicamente — histórico de transações é preservado.
import { api, apiUnwrap } from "../api";

/**
 * @param {number} id    id numérico da conta
 * @param {string} code  code (chave composta com id)
 */
export function remover(id, code) {
  return apiUnwrap(api.patch(`/contas/${id}/${code}/remover`));
}
