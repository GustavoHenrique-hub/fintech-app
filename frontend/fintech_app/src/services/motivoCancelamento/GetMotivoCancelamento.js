// GET endpoints do recurso /motivos-cancelamento (read-only).
//
// Backend: MotivoCancelamentoController. Response: MotivoCancelamentoResponseDTO.
import { api, apiUnwrap } from "../api";

/** Lista todos os motivos de cancelamento ativos. */
export function listar() {
  return apiUnwrap(api.get("/motivos-cancelamento"));
}

/** Busca um motivo pela chave composta (id, code). */
export function buscarPorChave(id, code) {
  return apiUnwrap(api.get(`/motivos-cancelamento/${id}/${code}`));
}
