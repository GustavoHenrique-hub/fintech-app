package com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoRequestDTO(
        @NotNull Long usuarioId,
        @NotNull String usuarioCode,
        @NotNull Long contaId,
        @NotNull String contaCode,
        @NotBlank String tipo,
        String descricao,
        @NotNull @DecimalMin("0.01") BigDecimal valor,
        @NotNull LocalDate dataTransacao,
        @NotNull Long categoriaId,
        String estabelecimento,
        @NotBlank String origem,
        String observacao
) {}
