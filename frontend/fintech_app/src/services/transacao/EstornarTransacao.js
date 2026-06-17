// PATCH /transacoes/{id}/{code}/estornar — estorna uma transação confirmada.
//
// Backend: TransacaoController#estornar. Idempotente: chamadas repetidas
// para a mesma chave retornam a transação já marcada com indEstorno='S'.
import { api, apiUnwrap } from "../api";

/**
 * @param {number} id    id numérico da transação
 * @param {string} code  code (chave composta com id)
 * @param {object} dto   EstornarTransacaoRequestDTO { motivo, observacao? }
 */
export function estornar(id, code, dto) {
  return apiUnwrap(api.patch(`/transacoes/${id}/${code}/estornar`, dto));
}
