// POST /bancos — cadastra novo banco no catálogo.
//
// Backend: BancoController#criar.
// Request: BancoRequestDTO  Response: BancoResponseDTO (HTTP 201).
import { api, apiUnwrap } from "../api";

/** @param {object} dto BancoRequestDTO { nome, code, ... } */
export function criar(dto) {
  return apiUnwrap(api.post("/bancos", dto));
}
