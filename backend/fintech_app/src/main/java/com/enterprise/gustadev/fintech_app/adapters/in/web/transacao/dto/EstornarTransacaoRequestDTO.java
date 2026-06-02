package com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EstornarTransacaoRequestDTO(
        @NotNull Long usuarioId,
        @NotBlank String usuarioCode,
        @NotNull Long contaId,
        @NotBlank String contaCode
) {}
