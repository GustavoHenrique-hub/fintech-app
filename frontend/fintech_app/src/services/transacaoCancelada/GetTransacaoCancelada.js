// GET endpoint do recurso /transacoes-canceladas.
//
// Backend: TransacaoCanceladaController. Response: TransacaoCanceladaResponseDTO[].
import { api, apiUnwrap } from "../api";

/** Lista cancelamentos de transações de um usuário. */
export function listarPorUsuario(usuarioId) {
  return apiUnwrap(api.get(`/transacoes-canceladas/usuario/${usuarioId}`));
}
