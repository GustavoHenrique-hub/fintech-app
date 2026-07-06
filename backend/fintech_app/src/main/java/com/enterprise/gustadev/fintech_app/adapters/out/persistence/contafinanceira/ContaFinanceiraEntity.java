package com.enterprise.gustadev.fintech_app.adapters.out.persistence.contafinanceira;

import com.enterprise.gustadev.fintech_app.adapters.out.persistence.banco.BancoEntity;
import com.enterprise.gustadev.fintech_app.adapters.out.persistence.usuario.UsuarioEntity;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "contas_financeiras")
@Getter
@Setter
@NoArgsConstructor
public class ContaFinanceiraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "contas_code", unique = true, nullable = false, length = 6)
    private String code;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "usuario_code", nullable = false)
    private String usuarioCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
            value = {
                    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id",
                            insertable = false, updatable = false),
                    @JoinColumn(name = "usuario_code", referencedColumnName = "usuario_code",
                            insertable = false, updatable = false)
            },
            foreignKey = @ForeignKey(name = "fk_contas_financeiras_usuario")
    )
    private UsuarioEntity usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoConta tipo;

    @Column(name = "banco_id", nullable = false)
    private Long bancoId;

    @Column(name = "banco_code", nullable = false, length = 6)
    private String bancoCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
            value = {
                    @JoinColumn(name = "banco_id", referencedColumnName = "banco_id",
                            insertable = false, updatable = false),
                    @JoinColumn(name = "banco_code", referencedColumnName = "banco_code",
                            insertable = false, updatable = false)
            },
            foreignKey = @ForeignKey(name = "fk_contas_financeiras_banco")
    )
    private BancoEntity banco;

    @Column(name = "saldo_inicial", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoInicial = BigDecimal.ZERO;

    @Column(name = "saldo_atual", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoAtual = BigDecimal.ZERO;

    @Column
    private Boolean padrao = false;

    @Column
    private Boolean ativa = true;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;

    @Column(name = "ind_delete", nullable = false, length = 1)
    private String indDelete = "N";

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    public static ContaFinanceiraEntity fromDomain(ContaFinanceira domain) {
        ContaFinanceiraEntity entity = new ContaFinanceiraEntity();
        entity.id = domain.getId();
        entity.code = domain.getCode();
        entity.usuarioId = domain.getUsuarioId();
        entity.usuarioCode = domain.getUsuarioCode();
        entity.tipo = domain.getTipo();
        entity.bancoId = domain.getBancoId();
        entity.bancoCode = domain.getBancoCode();
        entity.saldoInicial = domain.getSaldoInicial();
        entity.saldoAtual = domain.getSaldoAtual() != null ? domain.getSaldoAtual() : BigDecimal.ZERO;
        entity.padrao = domain.isPadrao();
        entity.ativa = domain.isAtiva();
        entity.criadoEm = domain.getCriadoEm();
        entity.atualizadoEm = domain.getAtualizadoEm();
        entity.indDelete = domain.getIndDelete() != null ? domain.getIndDelete() : "N";
        entity.deletedAt = domain.getDeletedAt();
        return entity;
    }

    public ContaFinanceira toDomain() {
        ContaFinanceira c = new ContaFinanceira(id, usuarioId, usuarioCode, tipo, bancoId, bancoCode, saldoInicial,
                saldoAtual != null ? saldoAtual : BigDecimal.ZERO,
                padrao != null && padrao, ativa != null && ativa, criadoEm, atualizadoEm,
                indDelete, deletedAt);
        c.setCode(code);
        return c;
    }
}
