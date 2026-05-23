package com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransacaoRequestDTO(
        @NotNull UUID usuarioId,
        @NotNull UUID contaId,
        UUID extratoId,
        @NotBlank String tipo,
        String descricaoOriginal,
        String descricaoUsuario,
        @NotNull @DecimalMin("0.01") BigDecimal valor,
        @NotNull LocalDate dataTransacao,
        UUID categoriaId,
        String subcategoria,
        String estabelecimento,
        @NotBlank String origem,
        String observacao
) {}
