package com.enterprise.gustadev.fintech_app.adapters.in.web.contafinanceira.dto;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ContaFinanceiraResponseDTO(
        Long id,
        String code,
        Long usuarioId,
        String tipo,
        Long bancoId,
        String bancoCode,
        BigDecimal saldoInicial,
        boolean padrao,
        boolean ativa,
        OffsetDateTime criadoEm
) {
    public static ContaFinanceiraResponseDTO fromDomain(ContaFinanceira domain) {
        return new ContaFinanceiraResponseDTO(
                domain.getId(),
                domain.getCode(),
                domain.getUsuarioId(),
                domain.getTipo().name(),
                domain.getBancoId(),
                domain.getBancoCode(),
                domain.getSaldoInicial(),
                domain.isPadrao(),
                domain.isAtiva(),
                domain.getCriadoEm()
        );
    }
}
