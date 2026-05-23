package com.enterprise.gustadev.fintech_app.adapters.in.web.consentimentolgpd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ConsentimentoLgpdRequestDTO(
        @NotNull UUID usuarioId,
        @NotBlank String tipo,
        @NotBlank String versaoPolitica,
        @NotNull boolean consentido,
        String ipOrigem
) {}
