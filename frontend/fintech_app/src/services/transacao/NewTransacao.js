// POST /transacoes — cria uma nova transação financeira.
//
// Backend: TransacaoController#criar.
// Request: TransacaoRequestDTO  Response: TransacaoResponseDTO (HTTP 201).
import { api, apiUnwrap } from "../api";

/**
 * @param {object} dto  TransacaoRequestDTO
 *   { usuarioId, contaId, extratoId?, tipo: "RECEITA"|"GASTO", valor,
 *     dataTransacao, descricaoUsuario?, categoriaId, subcategoria?,
 *     estabelecimento?, origem: "manual"|"pdf"|"api", observacao? }
 */
export function criar(dto) {
  return apiUnwrap(api.post("/transacoes", dto));
}
