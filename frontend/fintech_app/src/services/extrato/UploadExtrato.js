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
  // A instância `api` define Content-Type: application/json por padrão (ver api.js).
  // Nesse caso específico o corpo é multipart — sem este override, o transformRequest
  // do axios vê o Content-Type json e serializa o FormData como JSON (formDataToJSON),
  // quebrando o upload com 415 Unsupported Media Type. `undefined` remove o header
  // explícito e deixa o navegador definir "multipart/form-data; boundary=...".
  return apiUnwrap(api.post("/extratos/upload", formData, {
    headers: { "Content-Type": undefined },
  }));
}
