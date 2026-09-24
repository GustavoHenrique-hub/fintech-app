// Ações sobre o processamento de um extrato já importado.
//
// Backend: ExtratoController#reenviar e #cancelar.
//   · reenviar → POST  /extratos/{id}/{code}/reenviar — processa de novo o arquivo
//     armazenado de um extrato com erro (o upload recusa o mesmo arquivo pelo hash).
//   · cancelar → PATCH /extratos/{id}/{code}/cancelar — tira da fila um extrato
//     preso no processamento; ele vai para erro_classificacao e pode ser reenviado.
// Response: ExtratoResponseDTO.
import { api, apiUnwrap } from "../api";

/**
 * @param {number} id    id numérico do extrato
 * @param {string} code  code (chave composta com id)
 */
export function reenviar(id, code) {
  return apiUnwrap(api.post(`/extratos/${id}/${code}/reenviar`));
}

/**
 * @param {number} id    id numérico do extrato
 * @param {string} code  code (chave composta com id)
 */
export function cancelar(id, code) {
  return apiUnwrap(api.patch(`/extratos/${id}/${code}/cancelar`));
}
