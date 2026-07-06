package com.enterprise.gustadev.fintech_app.adapters.in.web.contafinanceira.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Dados para criar uma conta financeira vinculando um usuário a um banco existente.")
public record ContaFinanceiraRequestDTO(
        @Schema(description = "ID do usuário dono da conta", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Long usuarioId,

        @Schema(description = "Code do usuário dono da conta", example = "LPD056", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull String usuarioCode,

        @Schema(description = "Tipo da conta. Valores permitidos: corrente, poupanca, investimento, dinheiro etc.",
                example = "corrente", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String tipo,

        @Schema(description = "ID do banco previamente cadastrado em /bancos", example = "10",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Long bancoId,

        @Schema(description = "Código alfanumérico de 6 caracteres do banco (banco_code)",
                example = "NUBANK", minLength = 6, maxLength = 6,
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min = 6, max = 6) String bancoCode,

        @Schema(description = "Saldo inicial da conta (>= 0,00)", example = "1500.00",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull @DecimalMin("0.00") BigDecimal saldoInicial,

        @Schema(description = "Indica se esta é a conta padrão do usuário", example = "true")
        boolean padrao
) {}
