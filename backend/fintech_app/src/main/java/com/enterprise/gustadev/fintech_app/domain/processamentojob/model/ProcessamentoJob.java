package com.enterprise.gustadev.fintech_app.domain.processamentojob.model;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusJob;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoJob;
import com.enterprise.gustadev.fintech_app.domain.shared.util.CodeGenerator;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ProcessamentoJob {

    private Long id;
    private String code;
    private Long extratoId;
    private String extratoCode;
    private TipoJob tipo;
    private StatusJob status;
    private short tentativas;
    private short maxTentativas;
    private String payload;
    private String erroMensagem;
    private String workerId;
    private OffsetDateTime lockExpiresAt;
    private Long correlationId;
    private OffsetDateTime enfileiradoEm;
    private OffsetDateTime iniciadoEm;
    private OffsetDateTime concluidoEm;
    private OffsetDateTime proximoRetry;

    public ProcessamentoJob(Long id, String code, Long extratoId, String extratoCode,
                             TipoJob tipo, StatusJob status, short tentativas, short maxTentativas,
                             String payload, String erroMensagem, String workerId,
                             OffsetDateTime lockExpiresAt, Long correlationId,
                             OffsetDateTime enfileiradoEm, OffsetDateTime iniciadoEm,
                             OffsetDateTime concluidoEm, OffsetDateTime proximoRetry) {
        this.id = id;
        this.code = code;
        this.extratoId = extratoId;
        this.extratoCode = extratoCode;
        this.tipo = tipo;
        this.status = status;
        this.tentativas = tentativas;
        this.maxTentativas = maxTentativas;
        this.payload = payload;
        this.erroMensagem = erroMensagem;
        this.workerId = workerId;
        this.lockExpiresAt = lockExpiresAt;
        this.correlationId = correlationId;
        this.enfileiradoEm = enfileiradoEm;
        this.iniciadoEm = iniciadoEm;
        this.concluidoEm = concluidoEm;
        this.proximoRetry = proximoRetry;
    }

    public ProcessamentoJob(Long extratoId, String extratoCode, TipoJob tipo) {
        this(null, null, extratoId, extratoCode, tipo, StatusJob.enfileirado, (short) 0, (short) 3,
             null, null, null, null, null, null, null, null, null);
        this.code = CodeGenerator.gerar();
    }
}
