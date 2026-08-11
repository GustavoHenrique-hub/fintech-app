// GET endpoint do recurso /notificacoes.
//
// Backend: NotificacaoController. Response: NotificacaoResponseDTO[].
// Usa enum CanalNotificacao (PUSH | EMAIL | SMS).
import { api, apiUnwrap } from "../api";

/** Lista todas as notificações enviadas a um usuário. */
export function listarPorUsuario(usuarioId) {
  return apiUnwrap(api.get(`/notificacoes/usuario/${usuarioId}`));
}
