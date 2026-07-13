// POST /transacoes — cria uma nova transação financeira.
//
// Backend: TransacaoController#criar.
// Request: TransacaoRequestDTO  Response: TransacaoResponseDTO (HTTP 201).
import { api, apiUnwrap } from "../api";

/**
 * @param {object} dto  TransacaoRequestDTO
 *   { usuarioId, contaId, extratoId?, valor, dataTransacao, descricaoUsuario?,
 *     categoriaId, subcategoria?, estabelecimento?,
 *     origem: "manual"|"pdf"|"api", observacao? }
 *   A direção (receita/gasto) não é mais um campo próprio: é derivada da
 *   categoria no backend. `valor` deve ser positivo, exceto quando a
 *   categoria selecionada é do tipo "ambos" (ex.: "Outros"), caso em que o
 *   sinal decide a direção (negativo = gasto, positivo = receita).
 */
export function criar(dto) {
  return apiUnwrap(api.post("/transacoes", dto));
}
