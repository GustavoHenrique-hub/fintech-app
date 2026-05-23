package com.enterprise.gustadev.fintech_app.adapters.in.web.categoriapai.dto;

import com.enterprise.gustadev.fintech_app.domain.categoriapai.model.CategoriaPai;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CategoriaPaiResponseDTO(
        UUID id,
        UUID categoriaId,
        UUID paiId,
        OffsetDateTime criadoEm
) {
    public static CategoriaPaiResponseDTO fromDomain(CategoriaPai domain) {
        return new CategoriaPaiResponseDTO(
                domain.getId(),
                domain.getCategoriaId(),
                domain.getPaiId(),
                domain.getCriadoEm()
        );
    }
}
