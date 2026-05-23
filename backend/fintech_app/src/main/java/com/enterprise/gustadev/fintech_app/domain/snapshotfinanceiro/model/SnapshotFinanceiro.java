package com.enterprise.gustadev.fintech_app.domain.snapshotfinanceiro.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class SnapshotFinanceiro {

    private Long id;
    private Long usuarioId;
    private Long contaId;
    private short ano;
    private short mes;
    private BigDecimal saldoInicial;
    private BigDecimal totalReceitas;
    private BigDecimal totalGastos;
    private BigDecimal saldoFinal;
    private boolean fechado;
    private OffsetDateTime fechadoEm;

    public SnapshotFinanceiro(Long id, Long usuarioId, Long contaId, short ano, short mes,
                               BigDecimal saldoInicial, BigDecimal totalReceitas,
                               BigDecimal totalGastos, BigDecimal saldoFinal,
                               boolean fechado, OffsetDateTime fechadoEm) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.contaId = contaId;
        this.ano = ano;
        this.mes = mes;
        this.saldoInicial = saldoInicial;
        this.totalReceitas = totalReceitas;
        this.totalGastos = totalGastos;
        this.saldoFinal = saldoFinal;
        this.fechado = fechado;
        this.fechadoEm = fechadoEm;
    }

    public SnapshotFinanceiro(Long usuarioId, Long contaId, short ano, short mes,
                               BigDecimal saldoInicial) {
        this(null, usuarioId, contaId, ano, mes, saldoInicial,
             BigDecimal.ZERO, BigDecimal.ZERO, saldoInicial, false, null);
    }
}
