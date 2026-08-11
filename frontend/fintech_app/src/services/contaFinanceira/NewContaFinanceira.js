// POST /contas — cadastra nova conta financeira (vínculo usuário-banco).
//
// Backend: ContaFinanceiraController#criar.
// Request: ContaFinanceiraRequestDTO  Response: ContaFinanceiraResponseDTO (HTTP 201).
import { api, apiUnwrap } from "../api";

/**
 * @param {object} dto ContaFinanceiraRequestDTO
 *   { usuarioId, bancoId, nome, tipo: TipoConta, saldoInicial?, padrao?, ativa? }
 */
export function criar(dto) {
  return apiUnwrap(api.post("/contas", dto));
}
