// GET endpoints do recurso /bancos.
//
// Backend: BancoController. Response: BancoResponseDTO.
import { api, apiUnwrap } from "../api";

/** Lista todos os bancos cadastrados no catálogo. */
export function listar() {
  return apiUnwrap(api.get("/bancos"));
}

/** Busca um banco pela chave composta (id, code). */
export function buscarPorChave(id, code) {
  return apiUnwrap(api.get(`/bancos/${id}/${code}`));
}
