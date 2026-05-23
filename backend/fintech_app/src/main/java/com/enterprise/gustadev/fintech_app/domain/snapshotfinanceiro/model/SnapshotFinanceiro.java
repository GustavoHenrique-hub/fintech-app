package com.enterprise.gustadev.fintech_app.domain.snapshotfinanceiro.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class SnapshotFinanceiro {

    private UUID id;
    private UUID usuarioId;
    private UUID contaId;
    private short ano;
    private short mes;
    private BigDecimal saldoInicial;
    private BigDecimal totalReceitas;
    private BigDecimal totalGastos;
    private BigDecimal saldoFinal;
    private boolean fechado;
    private OffsetDateTime fechadoEm;

    public SnapshotFinanceiro(UUID id, UUID usuarioId, UUID contaId, short ano, short mes,
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

    public SnapshotFinanceiro(UUID usuarioId, UUID contaId, short ano, short mes,
                               BigDecimal saldoInicial) {
        this(null, usuarioId, contaId, ano, mes, saldoInicial,
             BigDecimal.ZERO, BigDecimal.ZERO, saldoInicial, false, null);
    }
}
