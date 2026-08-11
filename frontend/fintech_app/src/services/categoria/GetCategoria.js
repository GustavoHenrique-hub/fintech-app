// GET endpoints do recurso /categorias.
//
// Backend: CategoriaController. Response: CategoriaResponseDTO.
// Usa enum TipoCategoria (receita | gasto | ambos).
import { api, apiUnwrap } from "../api";

/** Lista as categorias padrão do sistema. */
export function listarPadrao() {
  return apiUnwrap(api.get("/categorias/padrao"));
}

/** Busca uma categoria pela chave composta (id, code). */
export function buscarPorChave(id, code) {
  return apiUnwrap(api.get(`/categorias/${id}/${code}`));
}
