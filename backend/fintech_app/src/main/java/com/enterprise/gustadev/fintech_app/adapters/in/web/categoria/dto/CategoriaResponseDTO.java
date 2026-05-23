package com.enterprise.gustadev.fintech_app.adapters.in.web.categoria.dto;

import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;

import java.util.UUID;

public record CategoriaResponseDTO(
        UUID id,
        String nome,
        UUID categoriaPaiId,
        String tipo,
        String icone,
        String corHex,
        boolean padrao,
        UUID usuarioId
) {
    public static CategoriaResponseDTO fromDomain(Categoria domain) {
        return new CategoriaResponseDTO(
                domain.getId(),
                domain.getNome(),
                domain.getCategoriaPaiId(),
                domain.getTipo().name(),
                domain.getIcone(),
                domain.getCorHex(),
                domain.isPadrao(),
                domain.getUsuarioId()
        );
    }
}
