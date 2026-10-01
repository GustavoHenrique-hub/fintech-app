package com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto;

import com.enterprise.gustadev.fintech_app.domain.usuario.model.CodigoVinculoTelegram;

import java.time.OffsetDateTime;

/**
 * {@code link} vem nulo quando {@code telegram.bot-username} não está configurado —
 * o app então mostra o comando {@code /vincular <codigo>} para o usuário digitar.
 */
public record CodigoVinculoTelegramResponseDTO(
        String codigo,
        String link,
        String botUsername,
        OffsetDateTime expiraEm
) {
    public static CodigoVinculoTelegramResponseDTO fromDomain(CodigoVinculoTelegram codigo, String botUsername) {
        boolean temBot = botUsername != null && !botUsername.isBlank();
        return new CodigoVinculoTelegramResponseDTO(
                codigo.getCodigo(),
                temBot ? "https://t.me/" + botUsername + "?start=" + codigo.getCodigo() : null,
                temBot ? botUsername : null,
                codigo.getExpiraEm()
        );
    }
}
