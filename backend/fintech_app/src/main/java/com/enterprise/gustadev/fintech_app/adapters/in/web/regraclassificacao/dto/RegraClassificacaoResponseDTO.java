package com.enterprise.gustadev.fintech_app.adapters.in.web.regraclassificacao.dto;

import com.enterprise.gustadev.fintech_app.domain.regraclassificacao.model.RegraClassificacao;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record RegraClassificacaoResponseDTO(
        UUID id,
        UUID usuarioId,
        String termo,
        UUID categoriaId,
        String subcategoria,
        BigDecimal scoreConfianca,
        int vezesAplicada,
        boolean congelada,
        String criadaPor,
        OffsetDateTime criadoEm
) {
    public static RegraClassificacaoResponseDTO fromDomain(RegraClassificacao domain) {
        return new RegraClassificacaoResponseDTO(
                domain.getId(),
                domain.getUsuarioId(),
                domain.getTermo(),
                domain.getCategoriaId(),
                domain.getSubcategoria(),
                domain.getScoreConfianca(),
                domain.getVezesAplicada(),
                domain.isCongelada(),
                domain.getCriadaPor().name(),
                domain.getCriadoEm()
        );
    }
}
