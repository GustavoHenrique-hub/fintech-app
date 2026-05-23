package com.enterprise.gustadev.fintech_app.adapters.in.web.categoriadousuario.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record VincularCategoriaUsuarioRequestDTO(
        @NotNull UUID usuarioId,
        @NotNull UUID categoriaId
) {}
