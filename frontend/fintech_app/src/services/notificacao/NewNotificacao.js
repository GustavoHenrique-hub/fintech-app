// POST /notificacoes — cria e envia notificação pelo canal especificado.
//
// Backend: NotificacaoController#criar.
// Request: CriarNotificacaoRequestDTO  Response: NotificacaoResponseDTO (HTTP 201).
import { api, apiUnwrap } from "../api";

/**
 * @param {object} dto CriarNotificacaoRequestDTO
 *   { usuarioId, canal: "PUSH"|"EMAIL"|"SMS", titulo, mensagem, payload? }
 */
export function criar(dto) {
  return apiUnwrap(api.post("/notificacoes", dto));
}
