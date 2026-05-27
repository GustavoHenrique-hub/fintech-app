package com.enterprise.gustadev.fintech_app.adapters.in.web.contafinanceira.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ContaFinanceiraRequestDTO(
        @NotNull Long usuarioId,
        @NotBlank String tipo,
        @NotNull Long bancoId,
        @NotBlank @Size(min = 6, max = 6) String bancoCode,
        @NotNull @DecimalMin("0.00") BigDecimal saldoInicial,
        boolean padrao
) {}
