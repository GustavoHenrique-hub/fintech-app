package com.enterprise.gustadev.fintech_app.adapters.in.web.regraclassificacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegraClassificacaoRequestDTO(
        @NotNull UUID usuarioId,
        @NotBlank String termo,
        @NotNull UUID categoriaId,
        String subcategoria,
        @NotBlank String criadaPor
) {}
