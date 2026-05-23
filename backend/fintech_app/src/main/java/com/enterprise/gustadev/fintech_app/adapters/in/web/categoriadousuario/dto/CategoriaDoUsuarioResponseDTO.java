package com.enterprise.gustadev.fintech_app.adapters.in.web.categoriadousuario.dto;

import com.enterprise.gustadev.fintech_app.domain.categoriadousuario.model.CategoriaDoUsuario;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CategoriaDoUsuarioResponseDTO(
        UUID id,
        UUID usuarioId,
        UUID categoriaId,
        boolean ativa,
        OffsetDateTime criadoEm
) {
    public static CategoriaDoUsuarioResponseDTO fromDomain(CategoriaDoUsuario domain) {
        return new CategoriaDoUsuarioResponseDTO(
                domain.getId(),
                domain.getUsuarioId(),
                domain.getCategoriaId(),
                domain.isAtiva(),
                domain.getCriadoEm()
        );
    }
}
