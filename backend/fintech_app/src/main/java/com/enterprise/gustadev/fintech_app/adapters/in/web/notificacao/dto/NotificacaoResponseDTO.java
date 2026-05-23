package com.enterprise.gustadev.fintech_app.adapters.in.web.notificacao.dto;

import com.enterprise.gustadev.fintech_app.domain.notificacao.model.Notificacao;

import java.time.OffsetDateTime;

public record NotificacaoResponseDTO(
        Long id,
        Long usuarioId,
        String canal,
        String tipo,
        String titulo,
        String mensagem,
        boolean enviada,
        OffsetDateTime enviadaEm,
        OffsetDateTime criadoEm
) {
    public static NotificacaoResponseDTO fromDomain(Notificacao domain) {
        return new NotificacaoResponseDTO(
                domain.getId(),
                domain.getUsuarioId(),
                domain.getCanal().name(),
                domain.getTipo(),
                domain.getTitulo(),
                domain.getMensagem(),
                domain.isEnviada(),
                domain.getEnviadaEm(),
                domain.getCriadoEm()
        );
    }
}
