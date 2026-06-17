// GET endpoints do recurso /extratos.
//
// Backend: ExtratoController. Response: ExtratoResponseDTO.
import { api, apiUnwrap } from "../api";

/** Lista os extratos importados por um usuário. */
export function listarPorUsuario(usuarioId) {
  return apiUnwrap(api.get(`/extratos/usuario/${usuarioId}`));
}

/** Busca um extrato pela chave composta (id, code). */
export function buscarPorChave(id, code) {
  return apiUnwrap(api.get(`/extratos/${id}/${code}`));
}
