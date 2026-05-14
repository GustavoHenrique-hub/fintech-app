package com.enterprise.gustadev.fintech_app.adapters.in.web.gasto.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GastoRequestDTO(
        @NotNull Long usuarioId,
        @NotNull @DecimalMin(value = "0.01") BigDecimal valor,
        @NotBlank String descricao,
        @NotBlank String categoria,
        @NotNull LocalDate data
) {
}
