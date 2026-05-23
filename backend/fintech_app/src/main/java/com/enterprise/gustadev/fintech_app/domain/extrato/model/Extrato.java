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
    private Long contaId;
    private String arquivoNome;
    private String arquivoUuid;
    private String hashArquivo;
    private String bancoDetectado;
    private Long parserVersaoId;
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

    public Extrato(Long id, Long usuarioId, Long contaId, String arquivoNome,
                   String arquivoUuid, String hashArquivo, String bancoDetectado,
                   Long parserVersaoId, BigDecimal scoreExtracao, LocalDate periodoInicio,
                   LocalDate periodoFim, StatusExtrato status, int totalLancamentos,
                   int lancamentosConfirmados, int lancamentosPendentes, int lancamentosIgnorados,
                   int versao, OffsetDateTime criadoEm, OffsetDateTime atualizadoEm) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.contaId = contaId;
        this.arquivoNome = arquivoNome;
        this.arquivoUuid = arquivoUuid;
        this.hashArquivo = hashArquivo;
        this.bancoDetectado = bancoDetectado;
        this.parserVersaoId = parserVersaoId;
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
    }

    public Extrato(Long usuarioId, Long contaId, String arquivoNome, String arquivoUuid, String hashArquivo) {
        this(null, usuarioId, contaId, arquivoNome, arquivoUuid, hashArquivo, null,
             null, null, null, null, StatusExtrato.upload_recebido,
             0, 0, 0, 0, 1, null, null);
        this.code = CodeGenerator.gerar();
    }

    public void validar() {
        if (usuarioId == null) {
            throw new ExtratoInvalidoException("UsuarioId é obrigatório");
        }
        if (contaId == null) {
            throw new ExtratoInvalidoException("ContaId é obrigatório");
        }
        if (arquivoUuid == null || arquivoUuid.isBlank()) {
            throw new ExtratoInvalidoException("ArquivoUuid é obrigatório");
        }
        if (hashArquivo == null || hashArquivo.isBlank()) {
            throw new ExtratoInvalidoException("Hash do arquivo é obrigatório");
        }
    }
}
