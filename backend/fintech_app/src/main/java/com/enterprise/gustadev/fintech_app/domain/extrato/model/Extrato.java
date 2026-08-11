package com.enterprise.gustadev.fintech_app.domain.extrato.model;

import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusExtrato;
import com.enterprise.gustadev.fintech_app.domain.shared.util.CodeGenerator;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
public class Extrato {

    private Long id;
    private String code;
    private Long usuarioId;
    private String usuarioCode;
    private Long contaId;
    private String contaCode;
    private Long transacaoId;
    private String transacaoCode;
    private String arquivoNome;
    private String arquivoUuid;
    private String hashArquivo;
    private String bancoDetectado;
    private Long parserVersaoId;
    private String parserVersaoCode;
    private BigDecimal scoreExtracao;
    private LocalDate periodoInicio;
    private LocalDate periodoFim;
    private StatusExtrato status;
    private int totalLancamentos;
    private int lancamentosConfirmados;
    private int lancamentosPendentes;
    private int lancamentosIgnorados;
    private int versao;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;
    private String indDelete;
    private OffsetDateTime deletedAt;

    public Extrato(Long id, Long usuarioId, String usuarioCode, Long contaId, String contaCode,
                   Long transacaoId, String transacaoCode, String arquivoNome,
                   String arquivoUuid, String hashArquivo, String bancoDetectado,
                   Long parserVersaoId, String parserVersaoCode, BigDecimal scoreExtracao,
                   LocalDate periodoInicio, LocalDate periodoFim, StatusExtrato status,
                   int totalLancamentos, int lancamentosConfirmados, int lancamentosPendentes,
                   int lancamentosIgnorados, int versao, OffsetDateTime criadoEm,
                   OffsetDateTime atualizadoEm, String indDelete, OffsetDateTime deletedAt) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioCode = usuarioCode;
        this.contaId = contaId;
        this.contaCode = contaCode;
        this.transacaoId = transacaoId;
        this.transacaoCode = transacaoCode;
        this.arquivoNome = arquivoNome;
        this.arquivoUuid = arquivoUuid;
        this.hashArquivo = hashArquivo;
        this.bancoDetectado = bancoDetectado;
        this.parserVersaoId = parserVersaoId;
        this.parserVersaoCode = parserVersaoCode;
        this.scoreExtracao = scoreExtracao;
        this.periodoInicio = periodoInicio;
        this.periodoFim = periodoFim;
        this.status = status;
        this.totalLancamentos = totalLancamentos;
        this.lancamentosConfirmados = lancamentosConfirmados;
        this.lancamentosPendentes = lancamentosPendentes;
        this.lancamentosIgnorados = lancamentosIgnorados;
        this.versao = versao;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.indDelete = indDelete != null ? indDelete : "N";
        this.deletedAt = deletedAt;
    }

    public Extrato(Long usuarioId, String usuarioCode, Long contaId, String contaCode,
                   String arquivoNome, String arquivoUuid, String hashArquivo) {
        this(null, usuarioId, usuarioCode, contaId, contaCode, null, null, arquivoNome, arquivoUuid,
             hashArquivo, null, null, null, null, null, null, StatusExtrato.upload_recebido,
             0, 0, 0, 0, 1, OffsetDateTime.now(), null, "N", null);
        this.code = CodeGenerator.gerar();
    }

    public void remover() {
        if ("S".equals(indDelete)) {
            throw new ExtratoInvalidoException("Extrato já foi removido");
        }
        this.indDelete = "S";
        this.deletedAt = OffsetDateTime.now();
    }

    /**
     * Reflete a confirmação manual de um lançamento pendente: move o contador de
     * pendente para confirmado e recalcula o status agregado do extrato —
     * concluido quando não sobra nenhum pendente, parcialmente_revisado enquanto
     * ainda houver.
     */
    public void confirmarLancamento() {
        if (lancamentosPendentes <= 0) {
            throw new ExtratoInvalidoException("Extrato não possui lançamentos pendentes de revisão");
        }
        this.lancamentosPendentes--;
        this.lancamentosConfirmados++;
        this.status = lancamentosPendentes == 0 ? StatusExtrato.concluido : StatusExtrato.parcialmente_revisado;
        this.atualizadoEm = OffsetDateTime.now();
    }

    public void validar() {
        if (usuarioId == null) {
            throw new ExtratoInvalidoException("UsuarioId é obrigatório");
        }
        if (usuarioCode == null || usuarioCode.isBlank()) {
            throw new ExtratoInvalidoException("UsuarioCode é obrigatório");
        }
        if (contaId == null) {
            throw new ExtratoInvalidoException("ContaId é obrigatório");
        }
        if (contaCode == null || contaCode.isBlank()) {
            throw new ExtratoInvalidoException("ContaCode é obrigatório");
        }
        if (arquivoUuid == null || arquivoUuid.isBlank()) {
            throw new ExtratoInvalidoException("ArquivoUuid é obrigatório");
        }
        if (hashArquivo == null || hashArquivo.isBlank()) {
            throw new ExtratoInvalidoException("Hash do arquivo é obrigatório");
        }
    }
}
