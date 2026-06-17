// GET endpoints do recurso /snapshots.
//
// Backend: SnapshotFinanceiroController. Response: SnapshotFinanceiroResponseDTO.
import { api, apiUnwrap } from "../api";

/** Lista todos os snapshots mensais de um usuário (todas as contas). */
export function listarPorUsuario(usuarioId) {
  return apiUnwrap(api.get(`/snapshots/usuario/${usuarioId}`));
}

/**
 * Busca o snapshot de um mês/ano específico. `contaId` é opcional —
 * se omitido, retorna o snapshot consolidado de todas as contas.
 *
 * @param {number}  usuarioId
 * @param {number}  ano        short (ex.: 2026)
 * @param {number}  mes        short (1-12)
 * @param {number} [contaId]   opcional
 */
export function buscarPorMes(usuarioId, ano, mes, contaId) {
  const params = { ano, mes };
  if (contaId !== undefined && contaId !== null) params.contaId = contaId;
  return apiUnwrap(api.get(`/snapshots/usuario/${usuarioId}/mes`, { params }));
}
