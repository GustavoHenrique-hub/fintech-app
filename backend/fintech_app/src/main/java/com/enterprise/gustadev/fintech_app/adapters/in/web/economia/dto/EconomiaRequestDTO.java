package com.enterprise.gustadev.fintech_app.adapters.in.web.economia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Dados para aportar ou resgatar do sub-saldo de \"Economias\" de uma conta financeira. " +
        "Não é receita nem gasto — é uma transferência interna entre saldoAtual e saldoEconomias da MESMA conta.")
public record EconomiaRequestDTO(
        @Schema(description = "Valor a ser movimentado (sempre positivo). " +
                "O sentido — reservar ou resgatar — é definido pelo endpoint chamado.",
                example = "150.00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull @Positive BigDecimal valor,

        @Schema(description = "Descrição livre da movimentação (opcional). Ex.: \"Reserva para viagem\".",
                example = "Reserva para viagem")
        String descricao
) {}
