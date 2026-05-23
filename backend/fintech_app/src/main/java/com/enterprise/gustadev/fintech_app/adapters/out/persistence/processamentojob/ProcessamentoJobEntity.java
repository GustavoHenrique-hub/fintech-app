package com.enterprise.gustadev.fintech_app.adapters.out.persistence.processamentojob;

import com.enterprise.gustadev.fintech_app.domain.processamentojob.model.ProcessamentoJob;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusJob;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoJob;
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

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "processamento_jobs")
@Getter
@Setter
@NoArgsConstructor
public class ProcessamentoJobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "extrato_id")
    private UUID extratoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoJob tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusJob status = StatusJob.enfileirado;

    @Column(nullable = false)
    private short tentativas = 0;

    @Column(name = "max_tentativas", nullable = false)
    private short maxTentativas = 3;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(name = "erro_mensagem", columnDefinition = "TEXT")
    private String erroMensagem;

    @Column(name = "worker_id", length = 100)
    private String workerId;

    @Column(name = "lock_expires_at")
    private OffsetDateTime lockExpiresAt;

    @Column(name = "correlation_id")
    private UUID correlationId;

    @Column(name = "enfileirado_em", nullable = false)
    private OffsetDateTime enfileiradoEm;

    @Column(name = "iniciado_em")
    private OffsetDateTime iniciadoEm;

    @Column(name = "concluido_em")
    private OffsetDateTime concluidoEm;

    @Column(name = "proximo_retry")
    private OffsetDateTime proximoRetry;

    public static ProcessamentoJobEntity fromDomain(ProcessamentoJob domain) {
        ProcessamentoJobEntity entity = new ProcessamentoJobEntity();
        entity.id = domain.getId();
        entity.extratoId = domain.getExtratoId();
        entity.tipo = domain.getTipo();
        entity.status = domain.getStatus();
        entity.tentativas = domain.getTentativas();
        entity.maxTentativas = domain.getMaxTentativas();
        entity.payload = domain.getPayload();
        entity.erroMensagem = domain.getErroMensagem();
        entity.workerId = domain.getWorkerId();
        entity.lockExpiresAt = domain.getLockExpiresAt();
        entity.correlationId = domain.getCorrelationId();
        entity.enfileiradoEm = domain.getEnfileiradoEm();
        entity.iniciadoEm = domain.getIniciadoEm();
        entity.concluidoEm = domain.getConcluidoEm();
        entity.proximoRetry = domain.getProximoRetry();
        return entity;
    }

    public ProcessamentoJob toDomain() {
        return new ProcessamentoJob(id, extratoId, tipo, status, tentativas, maxTentativas,
                payload, erroMensagem, workerId, lockExpiresAt, correlationId,
                enfileiradoEm, iniciadoEm, concluidoEm, proximoRetry);
    }
}
