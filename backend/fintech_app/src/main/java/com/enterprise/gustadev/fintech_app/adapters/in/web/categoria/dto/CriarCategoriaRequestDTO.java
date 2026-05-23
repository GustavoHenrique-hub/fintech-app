package com.enterprise.gustadev.fintech_app.adapters.in.web.categoria.dto;

import jakarta.validation.constraints.NotBlank;

public record CriarCategoriaRequestDTO(
        @NotBlank String nome,
        @NotBlank String tipo,
        String icone,
        String corHex
) {}
