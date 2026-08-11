package com.enterprise.gustadev.fintech_app.adapters.out.persistence.economia;

import com.enterprise.gustadev.fintech_app.adapters.out.persistence.contafinanceira.ContaFinanceiraEntity;
import com.enterprise.gustadev.fintech_app.domain.economia.model.MovimentacaoEconomia;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoMovimentacaoEconomia;
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
@Table(name = "movimentacoes_economia")
@Getter
@Setter
@NoArgsConstructor
public class MovimentacaoEconomiaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimentacao_economia_id")
    private Long id;

    @Column(name = "movimentacao_economia_code", unique = true, nullable = false, length = 6)
    private String code;

    @Column(name = "conta_id", nullable = false)
    private Long contaId;

    @Column(name = "conta_code", nullable = false, length = 6)
    private String contaCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
            value = {
                    @JoinColumn(name = "conta_id", referencedColumnName = "conta_id",
                            insertable = false, updatable = false),
                    @JoinColumn(name = "conta_code", referencedColumnName = "conta_code",
                            insertable = false, updatable = false)
            },
            foreignKey = @ForeignKey(name = "fk_movimentacoes_economia_conta")
    )
    private ContaFinanceiraEntity conta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoMovimentacaoEconomia tipo;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_movimentacao", nullable = false)
    private OffsetDateTime dataMovimentacao;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    public static MovimentacaoEconomiaEntity fromDomain(MovimentacaoEconomia domain) {
        MovimentacaoEconomiaEntity entity = new MovimentacaoEconomiaEntity();
        entity.id = domain.getId();
        entity.code = domain.getCode();
        entity.contaId = domain.getContaId();
        entity.contaCode = domain.getContaCode();
        entity.tipo = domain.getTipo();
        entity.valor = domain.getValor();
        entity.descricao = domain.getDescricao();
        entity.dataMovimentacao = domain.getDataMovimentacao();
        entity.criadoEm = domain.getCriadoEm();
        return entity;
    }

    public MovimentacaoEconomia toDomain() {
        return new MovimentacaoEconomia(id, code, contaId, contaCode, tipo, valor,
                descricao, dataMovimentacao, criadoEm);
    }
}
