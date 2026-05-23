package com.enterprise.gustadev.fintech_app.adapters.in.web.categoria.dto;

import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;


public record CategoriaResponseDTO(
        Long id,
        String code,
        String nome,
        String tipo,
        String icone,
        String corHex,
        boolean padrao
) {
    public static CategoriaResponseDTO fromDomain(Categoria domain) {
        return new CategoriaResponseDTO(
                domain.getId(),
                domain.getCode(),
                domain.getNome(),
                domain.getTipo().name(),
                domain.getIcone(),
                domain.getCorHex(),
                domain.isPadrao()
        );
    }
}
