// POST /categorias — cria nova categoria de transação.
//
// Backend: CategoriaController#criar.
// Request: CriarCategoriaRequestDTO  Response: CategoriaResponseDTO (HTTP 201).
import { api, apiUnwrap } from "../api";

/**
 * @param {object} dto CriarCategoriaRequestDTO
 *   { nome, tipo: "receita"|"gasto"|"ambos", icone, corHex, parentId? }
 */
export function criar(dto) {
  return apiUnwrap(api.post("/categorias", dto));
}
