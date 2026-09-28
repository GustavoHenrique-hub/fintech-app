// PATCH /transacoes/{id}/{code}/revisar — fecha a revisão manual de um
// lançamento importado.
//
// Backend: TransacaoController#revisar. O corpo é opcional: sem ele o lançamento
// é confirmado com a classificação que veio do extrato.
import { api, apiUnwrap } from "../api";

/**
 * @param {number} id    id numérico da transação
 * @param {string} code  code (chave composta com id)
 * @param {{ destino?: "GASTO"|"RECEITA"|"ECONOMIA", categoriaId?: number, categoriaCode?: string }} [escolha]
 *   destino ECONOMIA converte o valor em aporte no sub-saldo de economias da conta
 *   e tira o lançamento da aba Transações.
 */
export function revisar(id, code, escolha) {
  return apiUnwrap(api.patch(`/transacoes/${id}/${code}/revisar`, escolha ?? null));
}

/**
 * POST /transacoes/revisar-lote — "Revisar tudo": confirma vários lançamentos com a
 * escolha de cada um. Itens inválidos não interrompem os demais.
 *
 * @param {Array<{ id: number, code: string, destino?: string, categoriaId?: number, categoriaCode?: string }>} itens
 * @returns {Promise<{ revisadas: object[], falhas: Array<{ id: number, code: string, erro: string }> }>}
 */
export function revisarLote(itens) {
  return apiUnwrap(api.post("/transacoes/revisar-lote", { itens }));
}

/**
 * PATCH /transacoes/{id}/{code}/desfazer-revisao — "Estornar revisão": o lançamento
 * confirmado volta a pendente, sai do saldo e a confiança da IA cai.
 */
export function desfazerRevisao(id, code) {
  return apiUnwrap(api.patch(`/transacoes/${id}/${code}/desfazer-revisao`));
}
