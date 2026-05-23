package com.enterprise.gustadev.fintech_app.adapters.in.web.consentimentolgpd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record ConsentimentoLgpdRequestDTO(
        @NotNull Long usuarioId,
        @NotBlank String tipo,
        @NotBlank String versaoPolitica,
        @NotNull boolean consentido,
        String ipOrigem
) {}
