package com.enterprise.gustadev.fintech_app.adapters.out.persistence.contafinanceira;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "contas_financeiras")
@Getter
@Setter
@NoArgsConstructor
public class ContaFinanceiraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoConta tipo;

    @Column(length = 100)
    private String banco;

    @Column(name = "saldo_inicial", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoInicial = BigDecimal.ZERO;

    @Column
    private Boolean padrao = false;

    @Column
    private Boolean ativa = true;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;

    public static ContaFinanceiraEntity fromDomain(ContaFinanceira domain) {
        ContaFinanceiraEntity entity = new ContaFinanceiraEntity();
        entity.id = domain.getId();
        entity.usuarioId = domain.getUsuarioId();
        entity.nome = domain.getNome();
        entity.tipo = domain.getTipo();
        entity.banco = domain.getBanco();
        entity.saldoInicial = domain.getSaldoInicial();
        entity.padrao = domain.isPadrao();
        entity.ativa = domain.isAtiva();
        entity.criadoEm = domain.getCriadoEm();
        entity.atualizadoEm = domain.getAtualizadoEm();
        return entity;
    }

    public ContaFinanceira toDomain() {
        return new ContaFinanceira(id, usuarioId, nome, tipo, banco, saldoInicial,
                padrao != null && padrao, ativa != null && ativa, criadoEm, atualizadoEm);
    }
}
