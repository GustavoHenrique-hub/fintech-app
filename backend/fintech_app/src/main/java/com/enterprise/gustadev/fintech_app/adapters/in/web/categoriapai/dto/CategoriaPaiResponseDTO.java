package com.enterprise.gustadev.fintech_app.adapters.in.web.categoriapai.dto;

import com.enterprise.gustadev.fintech_app.domain.categoriapai.model.CategoriaPai;

import java.time.OffsetDateTime;

public record CategoriaPaiResponseDTO(
        Long id,
        Long categoriaId,
        Long paiId,
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
