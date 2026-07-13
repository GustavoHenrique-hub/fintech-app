package com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacao;

import com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoria.CategoriaEntity;
import com.enterprise.gustadev.fintech_app.adapters.out.persistence.contafinanceira.ContaFinanceiraEntity;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusRevisaoTransacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
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
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transacoes")
@Getter
@Setter
@NoArgsConstructor
public class TransacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transacoes_id")
    private Long id;

    @Column(name = "transacoes_code", unique = true, nullable = false, length = 6)
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
            foreignKey = @ForeignKey(name = "fk_transacoes_conta")
    )
    private ContaFinanceiraEntity conta;

    @Column(name = "ind_estorno", nullable = false, length = 1)
    private String indEstorno = "N";

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_transacao", nullable = false)
    private LocalDate dataTransacao;

    @Column(name = "categoria_id", nullable = false)
    private Long categoriaId;

    @Column(name = "categoria_code", nullable = false, length = 6)
    private String categoriaCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
            value = {
                    @JoinColumn(name = "categoria_id", referencedColumnName = "categoria_id",
                            insertable = false, updatable = false),
                    @JoinColumn(name = "categoria_code", referencedColumnName = "categoria_code",
                            insertable = false, updatable = false)
            },
            foreignKey = @ForeignKey(name = "fk_transacoes_categoria")
    )
    private CategoriaEntity categoria;

    @Column(length = 255)
    private String estabelecimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrigemTransacao origem = OrigemTransacao.manual;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_revisao", nullable = false, length = 30)
    private StatusRevisaoTransacao statusRevisao = StatusRevisaoTransacao.EXTRAIDA;

    @Column(name = "confianca_ia")
    private Short confiancaIa;

    @Column
    private Boolean recorrente = false;

    @Column(name = "periodo_recorrencia")
    private LocalDate periodoRecorrencia;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "estornado_at")
    private OffsetDateTime estornadoAt;

    @Column(nullable = false)
    private int versao = 1;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;

    @Column(name = "transacao_estornada_id")
    private Long transacaoEstornadaId;

    /**
     * Mapeia apenas os campos escalares. A associação {@code conta} é gravada pelo adapter
     * com referência gerenciada (EntityManager) para evitar tentar persistir instância
     * transiente de ContaFinanceiraEntity.
     */
    public static TransacaoEntity fromDomain(Transacao domain) {
        TransacaoEntity entity = new TransacaoEntity();
        entity.id = domain.getId();
        entity.code = domain.getCode();
        if (domain.getConta() != null) {
            entity.contaId = domain.getConta().getId();
            entity.contaCode = domain.getConta().getCode();
        }
        entity.indEstorno = domain.getIndEstorno();
        entity.descricao = domain.getDescricao();
        entity.valor = domain.getValor();
        entity.dataTransacao = domain.getDataTransacao();
        entity.categoriaId = domain.getCategoriaId();
        entity.categoriaCode = domain.getCategoriaCode();
        entity.estabelecimento = domain.getEstabelecimento();
        entity.origem = domain.getOrigem();
        entity.statusRevisao = domain.getStatusRevisao();
        entity.confiancaIa = domain.getConfiancaIa();
        entity.recorrente = domain.isRecorrente();
        entity.periodoRecorrencia = domain.getPeriodoRecorrencia();
        entity.observacao = domain.getObservacao();
        entity.estornadoAt = domain.getEstornadoAt();
        entity.deletedAt = domain.getDeletedAt();
        entity.versao = domain.getVersao();
        entity.criadoEm = domain.getCriadoEm();
        entity.atualizadoEm = domain.getAtualizadoEm();
        entity.transacaoEstornadaId = domain.getTransacaoEstornadaId();
        return entity;
    }

    public Transacao toDomain() {
        Transacao t = new Transacao(id, conta.toDomain(), indEstorno,
                descricao, valor, dataTransacao, categoriaId, categoriaCode, estabelecimento,
                origem, statusRevisao, confiancaIa, recorrente != null && recorrente, periodoRecorrencia,
                observacao, versao, criadoEm, atualizadoEm, estornadoAt);
        t.setCode(code);
        t.setTransacaoEstornadaId(transacaoEstornadaId);
        t.setDeletedAt(deletedAt);
        if (categoria != null) {
            t.setCategoriaTipo(categoria.getTipo());
        }
        return t;
    }
}
