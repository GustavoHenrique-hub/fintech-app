package com.enterprise.gustadev.fintech_app.adapters.in.web.categoria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CriarCategoriaRequestDTO(
        @NotNull UUID usuarioId,
        @NotBlank String nome,
        @NotBlank String tipo,
        String icone,
        String corHex,
        UUID categoriaPaiId
) {}
