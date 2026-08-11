package com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto;

import com.enterprise.gustadev.fintech_app.domain.transacao.model.ResumoPeriodo;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResumoPeriodoResponseDTO(
        Long usuarioId,
        Long contaId,
        LocalDate inicio,
        LocalDate fim,
        BigDecimal totalReceitas,
        BigDecimal totalGastos,
        BigDecimal saldoPeriodo
) {
    public static ResumoPeriodoResponseDTO fromDomain(ResumoPeriodo domain) {
        return new ResumoPeriodoResponseDTO(
                domain.usuarioId(),
                domain.contaId(),
                domain.inicio(),
                domain.fim(),
                domain.totalReceitas(),
                domain.totalGastos(),
                domain.saldoPeriodo()
        );
    }
}
