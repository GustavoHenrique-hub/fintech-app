package com.enterprise.gustadev.fintech_app.adapters.in.web.extrato.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record ExtratoRequestDTO(
        @NotNull Long usuarioId,
        @NotNull Long contaId,
        String arquivoNome,
        @NotBlank String arquivoUuid,
        @NotBlank String hashArquivo
) {}
