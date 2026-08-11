package com.enterprise.gustadev.fintech_app.domain.snapshotfinanceiro.model;

import com.enterprise.gustadev.fintech_app.domain.shared.util.CodeGenerator;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class SnapshotFinanceiro {

    private Long id;
    private String code;
    private Long usuarioId;
    private String usuarioCode;
    private Long contaId;
    private String contaCode;
    private short ano;
    private short mes;
    private BigDecimal saldoInicial;
    private BigDecimal totalReceitas;
    private BigDecimal totalGastos;
    private BigDecimal saldoFinal;
    private boolean fechado;
    private OffsetDateTime fechadoEm;

    public SnapshotFinanceiro(Long id, String code, Long usuarioId, String usuarioCode,
                               Long contaId, String contaCode, short ano, short mes,
                               BigDecimal saldoInicial, BigDecimal totalReceitas,
                               BigDecimal totalGastos, BigDecimal saldoFinal,
                               boolean fechado, OffsetDateTime fechadoEm) {
        this.id = id;
        this.code = code;
        this.usuarioId = usuarioId;
        this.usuarioCode = usuarioCode;
        this.contaId = contaId;
        this.contaCode = contaCode;
        this.ano = ano;
        this.mes = mes;
        this.saldoInicial = saldoInicial;
        this.totalReceitas = totalReceitas;
        this.totalGastos = totalGastos;
        this.saldoFinal = saldoFinal;
        this.fechado = fechado;
        this.fechadoEm = fechadoEm;
    }

    public SnapshotFinanceiro(Long usuarioId, String usuarioCode, Long contaId, String contaCode,
                               short ano, short mes, BigDecimal saldoInicial) {
        this(null, CodeGenerator.gerar(), usuarioId, usuarioCode, contaId, contaCode, ano, mes,
             saldoInicial, BigDecimal.ZERO, BigDecimal.ZERO, saldoInicial, false, null);
    }
}
