// PATCH /extratos/{id}/{code}/remover — soft delete do extrato.
//
// Backend: ExtratoController#remover. Marca indDelete='S'; preserva
// transações já importadas para fins de auditoria.
import { api, apiUnwrap } from "../api";

/**
 * @param {number} id    id numérico do extrato
 * @param {string} code  code (chave composta com id)
 */
export function remover(id, code) {
  return apiUnwrap(api.patch(`/extratos/${id}/${code}/remover`));
}
