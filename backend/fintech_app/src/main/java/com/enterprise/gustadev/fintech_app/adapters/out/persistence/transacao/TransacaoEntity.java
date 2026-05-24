package com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacao;

import com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoria.CategoriaEntity;
import com.enterprise.gustadev.fintech_app.adapters.out.persistence.contafinanceira.ContaFinanceiraEntity;
import com.enterprise.gustadev.fintech_app.adapters.out.persistence.extrato.ExtratoEntity;
import com.enterprise.gustadev.fintech_app.adapters.out.persistence.usuario.UsuarioEntity;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusRevisaoTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
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
    private Long id;

    @Column(name = "transacoes_code", unique = true, nullable = false, length = 6)
    private String code;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_transacoes_usuario"))
    private UsuarioEntity usuario;

    @Column(name = "conta_id", nullable = false)
    private Long contaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_transacoes_conta"))
    private ContaFinanceiraEntity conta;

    @Column(name = "extrato_id")
    private Long extratoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extrato_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_transacoes_extrato"))
    private ExtratoEntity extrato;

    @Column(name = "transacao_estorno_id")
    private Long transacaoEstornoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transacao_estorno_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_transacoes_estorno"))
    private TransacaoEntity transacaoEstorno;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoTransacao tipo;

    @Column(name = "descricao_original", columnDefinition = "TEXT")
    private String descricaoOriginal;

    @Column(name = "descricao_usuario", length = 255)
    private String descricaoUsuario;

    @Column(name = "descricao_normalizada", length = 255)
    private String descricaoNormalizada;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_transacao", nullable = false)
    private LocalDate dataTransacao;

    @Column(name = "data_lancamento", nullable = false)
    private OffsetDateTime dataLancamento;

    @Column(name = "categoria_id")
    private Long categoriaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_transacoes_categoria"))
    private CategoriaEntity categoria;

    @Column(length = 100)
    private String subcategoria;

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
    private boolean recorrente = false;

    @Column(name = "periodo_recorrencia", length = 20)
    private String periodoRecorrencia;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(nullable = false)
    private int versao = 1;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;

    public static TransacaoEntity fromDomain(Transacao domain) {
        TransacaoEntity entity = new TransacaoEntity();
        entity.id = domain.getId();
        entity.code = domain.getCode();
        entity.usuarioId = domain.getUsuarioId();
        entity.contaId = domain.getContaId();
        entity.extratoId = domain.getExtratoId();
        entity.transacaoEstornoId = domain.getTransacaoEstornoId();
        entity.tipo = domain.getTipo();
        entity.descricaoOriginal = domain.getDescricaoOriginal();
        entity.descricaoUsuario = domain.getDescricaoUsuario();
        entity.descricaoNormalizada = domain.getDescricaoNormalizada();
        entity.valor = domain.getValor();
        entity.dataTransacao = domain.getDataTransacao();
        entity.dataLancamento = domain.getDataLancamento();
        entity.categoriaId = domain.getCategoriaId();
        entity.subcategoria = domain.getSubcategoria();
        entity.estabelecimento = domain.getEstabelecimento();
        entity.origem = domain.getOrigem();
        entity.statusRevisao = domain.getStatusRevisao();
        entity.confiancaIa = domain.getConfiancaIa();
        entity.recorrente = domain.isRecorrente();
        entity.periodoRecorrencia = domain.getPeriodoRecorrencia();
        entity.observacao = domain.getObservacao();
        entity.deletedAt = domain.getDeletedAt();
        entity.versao = domain.getVersao();
        entity.criadoEm = domain.getCriadoEm();
        entity.atualizadoEm = domain.getAtualizadoEm();
        return entity;
    }

    public Transacao toDomain() {
        Transacao t = new Transacao(id, usuarioId, contaId, extratoId, transacaoEstornoId, tipo,
                descricaoOriginal, descricaoUsuario, descricaoNormalizada, valor,
                dataTransacao, dataLancamento, categoriaId, subcategoria, estabelecimento,
                origem, statusRevisao, confiancaIa, recorrente, periodoRecorrencia,
                observacao, deletedAt, versao, criadoEm, atualizadoEm);
        t.setCode(code);
        return t;
    }
}
