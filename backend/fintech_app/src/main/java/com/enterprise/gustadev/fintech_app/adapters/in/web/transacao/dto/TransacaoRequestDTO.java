package com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoRequestDTO(
        @NotNull Long usuarioId,
        @NotNull Long contaId,
        Long extratoId,
        @NotBlank String tipo,
        String descricaoOriginal,
        String descricaoUsuario,
        @NotNull @DecimalMin("0.01") BigDecimal valor,
        @NotNull LocalDate dataTransacao,
        Long categoriaId,
        String subcategoria,
        String estabelecimento,
        @NotBlank String origem,
        String observacao
) {}
