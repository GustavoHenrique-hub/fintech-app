// GET /contas/{idContas}/{contasCode}/economias
//
// Backend: EconomiaController#listar
// Response: MovimentacaoEconomiaResponseDTO[] (mais recente primeiro)
//   { id, code, contaId, contaCode, tipo: "APORTE"|"RESGATE",
//     valor, descricao, dataMovimentacao, criadoEm }
import { api, apiUnwrap } from "../api";

/**
 * Lista o histórico de movimentações de economias de uma conta (mais recente primeiro).
 * @param {number} contaId
 * @param {string} contaCode
 */
export function listarPorConta(contaId, contaCode) {
  return apiUnwrap(api.get(`/contas/${contaId}/${contaCode}/economias`));
}
