// GET endpoint do recurso /consentimentos.
//
// Backend: ConsentimentoLgpdController. Response: ConsentimentoLgpdResponseDTO[].
// Usa enum TipoConsentimentoLgpd.
import { api, apiUnwrap } from "../api";

/** Lista o histórico completo de consentimentos LGPD de um usuário. */
export function listarPorUsuario(usuarioId) {
  return apiUnwrap(api.get(`/consentimentos/usuario/${usuarioId}`));
}
