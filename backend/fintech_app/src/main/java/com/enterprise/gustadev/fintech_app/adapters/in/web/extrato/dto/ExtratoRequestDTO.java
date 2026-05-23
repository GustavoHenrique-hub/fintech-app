package com.enterprise.gustadev.fintech_app.adapters.in.web.extrato.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ExtratoRequestDTO(
        @NotNull UUID usuarioId,
        @NotNull UUID contaId,
        String arquivoNome,
        @NotBlank String arquivoUuid,
        @NotBlank String hashArquivo
) {}
