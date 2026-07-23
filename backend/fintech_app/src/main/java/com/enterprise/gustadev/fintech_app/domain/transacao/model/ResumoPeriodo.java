package com.enterprise.gustadev.fintech_app.domain.transacao.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResumoPeriodo(
        Long usuarioId,
        Long contaId,
        LocalDate inicio,
        LocalDate fim,
        BigDecimal totalReceitas,
        BigDecimal totalGastos
) {
    public BigDecimal saldoPeriodo() {
        return totalReceitas.subtract(totalGastos);
    }
}
