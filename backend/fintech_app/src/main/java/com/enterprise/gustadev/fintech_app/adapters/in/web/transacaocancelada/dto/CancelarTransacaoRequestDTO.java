package com.enterprise.gustadev.fintech_app.adapters.in.web.transacaocancelada.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CancelarTransacaoRequestDTO(
        @NotNull Long transacaoId,
        @NotNull Long motivoId,
        @NotBlank String canceladoPor,
        @NotNull @Positive BigDecimal valorOriginal,
        String observacao,
        String ipOrigem
) {}
