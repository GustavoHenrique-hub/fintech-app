package com.enterprise.gustadev.fintech_app.adapters.out.persistence.extrato;

import com.enterprise.gustadev.fintech_app.adapters.out.persistence.contafinanceira.ContaFinanceiraEntity;
import com.enterprise.gustadev.fintech_app.adapters.out.persistence.parserversao.ParserVersaoEntity;
import com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacao.TransacaoEntity;
import com.enterprise.gustadev.fintech_app.adapters.out.persistence.usuario.UsuarioEntity;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusExtrato;
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
@Table(name = "extratos")
@Getter
@Setter
@NoArgsConstructor
public class ExtratoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "extrato_id")
    private Long id;

    @Column(name = "extrato_code", unique = true, nullable = false, length = 6)
    private String code;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "usuario_code", nullable = false, length = 6)
    private String usuarioCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
            value = {
                    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id",
                            insertable = false, updatable = false),
                    @JoinColumn(name = "usuario_code", referencedColumnName = "usuario_code",
                            insertable = false, updatable = false)
            },
            foreignKey = @ForeignKey(name = "fk_extratos_usuario")
    )
    private UsuarioEntity usuario;

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
            foreignKey = @ForeignKey(name = "fk_extratos_conta")
    )
    private ContaFinanceiraEntity conta;

    @Column(name = "transacao_id")
    private Long transacaoId;

    @Column(name = "transacao_code", length = 6)
    private String transacaoCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
            value = {
                    @JoinColumn(name = "transacao_id", referencedColumnName = "transacoes_id",
                            insertable = false, updatable = false),
                    @JoinColumn(name = "transacao_code", referencedColumnName = "transacoes_code",
                            insertable = false, updatable = false)
            },
            foreignKey = @ForeignKey(name = "fk_extratos_transacao")
    )
    private TransacaoEntity transacao;

    @Column(name = "arquivo_nome", length = 255)
    private String arquivoNome;

    @Column(name = "arquivo_uuid", nullable = false, length = 255)
    private String arquivoUuid;

    @Column(name = "hash_arquivo", nullable = false, length = 64)
    private String hashArquivo;

    @Column(name = "banco_detectado", length = 100)
    private String bancoDetectado;

    @Column(name = "parser_versao_id")
    private Long parserVersaoId;

    @Column(name = "parser_versao_code", length = 6)
    private String parserVersaoCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
            value = {
                    @JoinColumn(name = "parser_versao_id", referencedColumnName = "parser_versao_id",
                            insertable = false, updatable = false),
                    @JoinColumn(name = "parser_versao_code", referencedColumnName = "parser_versao_code",
                            insertable = false, updatable = false)
            },
            foreignKey = @ForeignKey(name = "fk_extratos_parser_versao")
    )
    private ParserVersaoEntity parserVersao;

    @Column(name = "score_extracao", precision = 4, scale = 3)
    private BigDecimal scoreExtracao;

    @Column(name = "periodo_inicio")
    private LocalDate periodoInicio;

    @Column(name = "periodo_fim")
    private LocalDate periodoFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusExtrato status = StatusExtrato.upload_recebido;

    @Column(name = "total_lancamentos", nullable = false)
    private int totalLancamentos = 0;

    @Column(name = "lancamentos_confirmados", nullable = false)
    private int lancamentosConfirmados = 0;

    @Column(name = "lancamentos_pendentes", nullable = false)
    private int lancamentosPendentes = 0;

    @Column(name = "lancamentos_ignorados", nullable = false)
    private int lancamentosIgnorados = 0;

    @Column(nullable = false)
    private int versao = 1;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;

    @Column(name = "ind_delete", nullable = false, length = 1)
    private String indDelete = "N";

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    public static ExtratoEntity fromDomain(Extrato domain) {
        ExtratoEntity entity = new ExtratoEntity();
        entity.id = domain.getId();
        entity.code = domain.getCode();
        entity.usuarioId = domain.getUsuarioId();
        entity.usuarioCode = domain.getUsuarioCode();
        entity.contaId = domain.getContaId();
        entity.contaCode = domain.getContaCode();
        entity.transacaoId = domain.getTransacaoId();
        entity.transacaoCode = domain.getTransacaoCode();
        entity.arquivoNome = domain.getArquivoNome();
        entity.arquivoUuid = domain.getArquivoUuid();
        entity.hashArquivo = domain.getHashArquivo();
        entity.bancoDetectado = domain.getBancoDetectado();
        entity.parserVersaoId = domain.getParserVersaoId();
        entity.parserVersaoCode = domain.getParserVersaoCode();
        entity.scoreExtracao = domain.getScoreExtracao();
        entity.periodoInicio = domain.getPeriodoInicio();
        entity.periodoFim = domain.getPeriodoFim();
        entity.status = domain.getStatus();
        entity.totalLancamentos = domain.getTotalLancamentos();
        entity.lancamentosConfirmados = domain.getLancamentosConfirmados();
        entity.lancamentosPendentes = domain.getLancamentosPendentes();
        entity.lancamentosIgnorados = domain.getLancamentosIgnorados();
        entity.versao = domain.getVersao();
        entity.criadoEm = domain.getCriadoEm();
        entity.atualizadoEm = domain.getAtualizadoEm();
        entity.indDelete = domain.getIndDelete() != null ? domain.getIndDelete() : "N";
        entity.deletedAt = domain.getDeletedAt();
        return entity;
    }

    public Extrato toDomain() {
        Extrato e = new Extrato(id, usuarioId, usuarioCode, contaId, contaCode, transacaoId, transacaoCode,
                arquivoNome, arquivoUuid, hashArquivo, bancoDetectado, parserVersaoId, parserVersaoCode,
                scoreExtracao, periodoInicio, periodoFim, status, totalLancamentos, lancamentosConfirmados,
                lancamentosPendentes, lancamentosIgnorados, versao, criadoEm, atualizadoEm, indDelete, deletedAt);
        e.setCode(code);
        return e;
    }
}
