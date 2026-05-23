package com.enterprise.gustadev.fintech_app.adapters.in.web.consentimentolgpd.dto;

import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.model.ConsentimentoLgpd;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ConsentimentoLgpdResponseDTO(
        UUID id,
        UUID usuarioId,
        String tipo,
        String versaoPolitica,
        boolean consentido,
        OffsetDateTime criadoEm,
        OffsetDateTime revogadoEm
) {
    public static ConsentimentoLgpdResponseDTO fromDomain(ConsentimentoLgpd domain) {
        return new ConsentimentoLgpdResponseDTO(
                domain.getId(),
                domain.getUsuarioId(),
                domain.getTipo().name(),
                domain.getVersaoPolitica(),
                domain.isConsentido(),
                domain.getCriadoEm(),
                domain.getRevogadoEm()
        );
    }
}
