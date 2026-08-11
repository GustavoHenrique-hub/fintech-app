// GET /transacoes/resumo-periodo — soma receitas/gastos de uma conta num intervalo de datas.
//
// Backend: TransacaoController#resumoPeriodo. Response: ResumoPeriodoResponseDTO.
import { api, apiUnwrap } from "../api";

/**
 * @param {number} usuarioId
 * @param {string} usuarioCode
 * @param {number} contaId
 * @param {string} contaCode
 * @param {string} inicio  "yyyy-MM-dd"
 * @param {string} fim     "yyyy-MM-dd"
 */
export function buscarResumoPeriodo(usuarioId, usuarioCode, contaId, contaCode, inicio, fim) {
  return apiUnwrap(api.get("/transacoes/resumo-periodo", {
    params: { usuarioId, usuarioCode, contaId, contaCode, inicio, fim },
  }));
}
