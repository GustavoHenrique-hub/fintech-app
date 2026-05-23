package com.enterprise.gustadev.fintech_app.adapters.out.persistence.snapshotfinanceiro;

import com.enterprise.gustadev.fintech_app.domain.snapshotfinanceiro.model.SnapshotFinanceiro;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "snapshots_financeiros")
@Getter
@Setter
@NoArgsConstructor
public class SnapshotFinanceiroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "conta_id")
    private Long contaId;

    @Column(nullable = false)
    private short ano;

    @Column(nullable = false)
    private short mes;

    @Column(name = "saldo_inicial", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoInicial;

    @Column(name = "total_receitas", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalReceitas = BigDecimal.ZERO;

    @Column(name = "total_gastos", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalGastos = BigDecimal.ZERO;

    @Column(name = "saldo_final", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoFinal;

    @Column(nullable = false)
    private boolean fechado = false;

    @Column(name = "fechado_em")
    private OffsetDateTime fechadoEm;

    public static SnapshotFinanceiroEntity fromDomain(SnapshotFinanceiro domain) {
        SnapshotFinanceiroEntity entity = new SnapshotFinanceiroEntity();
        entity.id = domain.getId();
        entity.usuarioId = domain.getUsuarioId();
        entity.contaId = domain.getContaId();
        entity.ano = domain.getAno();
        entity.mes = domain.getMes();
        entity.saldoInicial = domain.getSaldoInicial();
        entity.totalReceitas = domain.getTotalReceitas();
        entity.totalGastos = domain.getTotalGastos();
        entity.saldoFinal = domain.getSaldoFinal();
        entity.fechado = domain.isFechado();
        entity.fechadoEm = domain.getFechadoEm();
        return entity;
    }

    public SnapshotFinanceiro toDomain() {
        return new SnapshotFinanceiro(id, usuarioId, contaId, ano, mes,
                saldoInicial, totalReceitas, totalGastos, saldoFinal, fechado, fechadoEm);
    }
}
