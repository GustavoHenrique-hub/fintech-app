// Vínculo do Telegram com a conta do usuário logado.
//
// Backend: TelegramVinculoController. O usuário vem da sessão (Bearer), não do path.
import { api, apiUnwrap } from "../api";

/**
 * POST /usuarios/me/telegram/codigo-vinculo — gera código de uso único (10 min).
 * Exige consentimento LGPD `bot_telegram` ativo.
 * @returns {Promise<{codigo: string, link: string|null, botUsername: string|null, expiraEm: string}>}
 */
export function gerarCodigoVinculoTelegram() {
  return apiUnwrap(api.post("/usuarios/me/telegram/codigo-vinculo"));
}

/** DELETE /usuarios/me/telegram — remove o chat vinculado. */
export function desvincularTelegram() {
  return apiUnwrap(api.delete("/usuarios/me/telegram"));
}
