// POST /extratos — registra metadados de extrato bancário importado.
//
// Backend: ExtratoController#criar.
// Request: ExtratoRequestDTO  Response: ExtratoResponseDTO (HTTP 201).
//
// NOTA: este endpoint registra apenas os metadados; o upload do PDF/CSV em si
// usa outro fluxo (multipart) que ainda não está exposto na controller atual.
import { api, apiUnwrap } from "../api";

/**
 * @param {object} dto ExtratoRequestDTO
 *   { usuarioId, contaId, nomeArquivo, periodoInicio, periodoFim, ... }
 */
export function criar(dto) {
  return apiUnwrap(api.post("/extratos", dto));
}
