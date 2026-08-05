// POST /extratos/upload — envia o arquivo do extrato bancário (multipart).
//
// Backend: ExtratoController#upload. Aceita PDF, CSV, TXT, XLS ou XLSX; extrai os
// lançamentos e já cria as transações com status PENDENTE_REVISAO.
// Response: ExtratoResponseDTO (HTTP 201).
import { api, apiUnwrap } from "../api";

/**
 * @param {number} usuarioId
 * @param {number} contaId
 * @param {File} arquivo
 */
export function upload(usuarioId, contaId, arquivo) {
  const formData = new FormData();
  formData.append("usuarioId", usuarioId);
  formData.append("contaId", contaId);
  formData.append("arquivo", arquivo);
  return apiUnwrap(api.post("/extratos/upload", formData));
}
