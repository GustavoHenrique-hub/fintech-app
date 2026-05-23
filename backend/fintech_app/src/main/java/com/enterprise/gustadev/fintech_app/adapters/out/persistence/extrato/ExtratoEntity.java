package com.enterprise.gustadev.fintech_app.adapters.out.persistence.extrato;

import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusExtrato;
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
    private Long id;

    @Column(name = "extratos_code", unique = true, nullable = false, length = 6)
    private String code;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "conta_id", nullable = false)
    private Long contaId;

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

    public static ExtratoEntity fromDomain(Extrato domain) {
        ExtratoEntity entity = new ExtratoEntity();
        entity.id = domain.getId();
        entity.code = domain.getCode() != null ? domain.getCode()
                : Long.toHexString(System.nanoTime()).substring(0, 6).toUpperCase();
        entity.usuarioId = domain.getUsuarioId();
        entity.contaId = domain.getContaId();
        entity.arquivoNome = domain.getArquivoNome();
        entity.arquivoUuid = domain.getArquivoUuid();
        entity.hashArquivo = domain.getHashArquivo();
        entity.bancoDetectado = domain.getBancoDetectado();
        entity.parserVersaoId = domain.getParserVersaoId();
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
        return entity;
    }

    public Extrato toDomain() {
        Extrato e = new Extrato(id, usuarioId, contaId, arquivoNome, arquivoUuid, hashArquivo,
                bancoDetectado, parserVersaoId, scoreExtracao, periodoInicio, periodoFim,
                status, totalLancamentos, lancamentosConfirmados, lancamentosPendentes,
                lancamentosIgnorados, versao, criadoEm, atualizadoEm);
        e.setCode(code);
        return e;
    }
}
