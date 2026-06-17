// POST /transacoes-canceladas — registra cancelamento de transação.
//
// Backend: TransacaoCanceladaController#criar.
// Request: CancelarTransacaoRequestDTO  Response: TransacaoCanceladaResponseDTO (HTTP 201).
// Usa enum CanceladoPor.
import { api, apiUnwrap } from "../api";

/**
 * @param {object} dto CancelarTransacaoRequestDTO
 *   { transacaoId, motivoCancelamentoId, canceladoPor: CanceladoPor,
 *     observacao?, usuarioCanceladorId }
 */
export function criar(dto) {
  return apiUnwrap(api.post("/transacoes-canceladas", dto));
}
