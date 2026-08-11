package com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacaocancelada;

import com.enterprise.gustadev.fintech_app.adapters.out.persistence.motivocancelamento.MotivoCancelamentoEntity;
import com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacao.TransacaoEntity;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.CanceladoPor;
import com.enterprise.gustadev.fintech_app.domain.transacaocancelada.model.TransacaoCancelada;
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
@Table(name = "transacoes_canceladas")
@Getter
@Setter
@NoArgsConstructor
public class TransacaoCanceladaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transacao_cancelada_id")
    private Long id;

    @Column(name = "transacao_cancelada_code", unique = true, nullable = false, length = 6)
    private String code;

    @Column(name = "transacao_id", nullable = false)
    private Long transacaoId;

    @Column(name = "transacao_code", nullable = false, length = 6)
    private String transacaoCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
            value = {
                    @JoinColumn(name = "transacao_id", referencedColumnName = "transacoes_id",
                            insertable = false, updatable = false),
                    @JoinColumn(name = "transacao_code", referencedColumnName = "transacoes_code",
                            insertable = false, updatable = false)
            },
            foreignKey = @ForeignKey(name = "fk_transacoes_canceladas_transacao")
    )
    private TransacaoEntity transacao;

    @Column(name = "motivo_id", nullable = false)
    private Long motivoId;

    @Column(name = "motivo_code", nullable = false, length = 6)
    private String motivoCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
            value = {
                    @JoinColumn(name = "motivo_id", referencedColumnName = "motivo_id",
                            insertable = false, updatable = false),
                    @JoinColumn(name = "motivo_code", referencedColumnName = "motivo_code",
                            insertable = false, updatable = false)
            },
            foreignKey = @ForeignKey(name = "fk_transacoes_canceladas_motivo")
    )
    private MotivoCancelamentoEntity motivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "cancelado_por", nullable = false, length = 20)
    private CanceladoPor canceladoPor = CanceladoPor.usuario;

    @Column(name = "valor_original", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorOriginal;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "ip_origem", length = 45)
    private String ipOrigem;

    @Column(name = "cancelado_em", nullable = false)
    private OffsetDateTime canceladoEm;

    public static TransacaoCanceladaEntity fromDomain(TransacaoCancelada domain) {
        TransacaoCanceladaEntity entity = new TransacaoCanceladaEntity();
        entity.id = domain.getId();
        entity.code = domain.getCode();
        entity.transacaoId = domain.getTransacaoId();
        entity.transacaoCode = domain.getTransacaoCode();
        entity.motivoId = domain.getMotivoId();
        entity.motivoCode = domain.getMotivoCode();
        entity.canceladoPor = domain.getCanceladoPor();
        entity.valorOriginal = domain.getValorOriginal();
        entity.observacao = domain.getObservacao();
        entity.ipOrigem = domain.getIpOrigem();
        entity.canceladoEm = domain.getCanceladoEm();
        return entity;
    }

    public TransacaoCancelada toDomain() {
        TransacaoCancelada t = new TransacaoCancelada(id, transacaoId, transacaoCode, motivoId, motivoCode,
                canceladoPor, valorOriginal, observacao, ipOrigem, canceladoEm);
        t.setCode(code);
        return t;
    }
}
