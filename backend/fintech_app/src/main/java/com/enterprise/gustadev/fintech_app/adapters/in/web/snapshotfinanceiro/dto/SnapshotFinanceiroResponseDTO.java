package com.enterprise.gustadev.fintech_app.adapters.in.web.snapshotfinanceiro.dto;

import com.enterprise.gustadev.fintech_app.domain.snapshotfinanceiro.model.SnapshotFinanceiro;

import java.math.BigDecimal;

public record SnapshotFinanceiroResponseDTO(
        Long id,
        Long usuarioId,
        Long contaId,
        short ano,
        short mes,
        BigDecimal saldoInicial,
        BigDecimal totalReceitas,
        BigDecimal totalGastos,
        BigDecimal saldoFinal,
        boolean fechado
) {
    public static SnapshotFinanceiroResponseDTO fromDomain(SnapshotFinanceiro domain) {
        return new SnapshotFinanceiroResponseDTO(
                domain.getId(),
                domain.getUsuarioId(),
                domain.getContaId(),
                domain.getAno(),
                domain.getMes(),
                domain.getSaldoInicial(),
                domain.getTotalReceitas(),
                domain.getTotalGastos(),
                domain.getSaldoFinal(),
                domain.isFechado()
        );
    }
}
