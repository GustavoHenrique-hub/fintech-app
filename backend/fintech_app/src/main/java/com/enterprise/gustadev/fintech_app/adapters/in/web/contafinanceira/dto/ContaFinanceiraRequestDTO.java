package com.enterprise.gustadev.fintech_app.adapters.in.web.contafinanceira.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ContaFinanceiraRequestDTO(
        @NotNull Long usuarioId,
        @NotBlank String nome,
        @NotBlank String tipo,
        String banco,
        @NotNull @DecimalMin("0.00") BigDecimal saldoInicial,
        boolean padrao
) {}
