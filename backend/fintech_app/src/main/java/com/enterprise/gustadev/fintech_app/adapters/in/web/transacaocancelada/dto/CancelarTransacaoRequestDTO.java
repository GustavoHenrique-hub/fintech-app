package com.enterprise.gustadev.fintech_app.adapters.in.web.transacaocancelada.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CancelarTransacaoRequestDTO(
        @NotNull UUID transacaoId,
        @NotNull UUID usuarioId,
        @NotNull UUID contaId,
        @NotNull UUID motivoId,
        @NotBlank String canceladoPor,
        @NotNull @Positive BigDecimal valorOriginal,
        String observacao,
        String ipOrigem
) {}
