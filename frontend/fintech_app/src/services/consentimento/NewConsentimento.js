// POST /consentimentos — registra consentimento ou revogação LGPD.
//
// Backend: ConsentimentoLgpdController#registrar.
// Request: ConsentimentoLgpdRequestDTO  Response: ConsentimentoLgpdResponseDTO (HTTP 201).
import { api, apiUnwrap } from "../api";

/**
 * @param {object} dto ConsentimentoLgpdRequestDTO
 *   { usuarioId, tipo: TipoConsentimentoLgpd, aceito: boolean, versao, ip?, userAgent? }
 */
export function registrar(dto) {
  return apiUnwrap(api.post("/consentimentos", dto));
}
