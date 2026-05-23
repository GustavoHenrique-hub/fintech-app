package com.enterprise.gustadev.fintech_app.adapters.in.web.categoriadousuario.dto;

import jakarta.validation.constraints.NotNull;


public record VincularCategoriaUsuarioRequestDTO(
        @NotNull Long usuarioId,
        @NotNull Long categoriaId
) {}
