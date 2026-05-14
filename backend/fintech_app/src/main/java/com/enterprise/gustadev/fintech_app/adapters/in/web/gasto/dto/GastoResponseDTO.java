package com.enterprise.gustadev.fintech_app.adapters.in.web.gasto.dto;

import com.enterprise.gustadev.fintech_app.domain.gasto.model.Gasto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GastoResponseDTO(
        Long id,
        Long usuarioId,
        BigDecimal valor,
        String descricao,
        String categoria,
        LocalDate data
) {
    public static GastoResponseDTO fromDomain(Gasto gasto) {
        return new GastoResponseDTO(
                gasto.getId(),
                gasto.getUsuarioId(),
                gasto.getValor(),
                gasto.getDescricao(),
                gasto.getCategoria().name(),
                gasto.getData()
        );
    }
}
