// POST /contas/{idContas}/{contasCode}/economias/aporte
// POST /contas/{idContas}/{contasCode}/economias/resgate
//
// Backend: EconomiaController#aportar / EconomiaController#resgatar
// Request: { valor: number, descricao?: string }
// Response: MovimentacaoEconomiaResponseDTO (HTTP 201)
//   { id, code, contaId, contaCode, tipo: "APORTE"|"RESGATE",
//     valor, descricao, dataMovimentacao, criadoEm }
import { api, apiUnwrap } from "../api";

/**
 * Guarda dinheiro: desconta de saldoAtual e credita em saldoEconomias.
 * @param {number} contaId
 * @param {string} contaCode
 * @param {{ valor: number, descricao?: string }} dto
 */
export function aportar(contaId, contaCode, dto) {
  return apiUnwrap(api.post(`/contas/${contaId}/${contaCode}/economias/aporte`, dto));
}

/**
 * Resgata dinheiro: debita de saldoEconomias e credita em saldoAtual.
 * @param {number} contaId
 * @param {string} contaCode
 * @param {{ valor: number, descricao?: string }} dto
 */
export function resgatar(contaId, contaCode, dto) {
  return apiUnwrap(api.post(`/contas/${contaId}/${contaCode}/economias/resgate`, dto));
}
