// PATCH /transacoes/{id}/{code}/revisar — confirma a revisão manual de um
// lançamento importado (PENDENTE_REVISAO → CONFIRMADA).
//
// Backend: TransacaoController#revisar.
import { api, apiUnwrap } from "../api";

/**
 * @param {number} id    id numérico da transação
 * @param {string} code  code (chave composta com id)
 */
export function revisar(id, code) {
  return apiUnwrap(api.patch(`/transacoes/${id}/${code}/revisar`));
}
