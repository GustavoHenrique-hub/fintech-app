package com.enterprise.gustadev.fintech_app.adapters.out.persistence.processamentojob;

import com.enterprise.gustadev.fintech_app.adapters.out.persistence.extrato.ExtratoEntity;
import com.enterprise.gustadev.fintech_app.domain.processamentojob.model.ProcessamentoJob;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusJob;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoJob;
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

import java.time.OffsetDateTime;

@Entity
@Table(name = "processamento_jobs")
@Getter
@Setter
@NoArgsConstructor
public class ProcessamentoJobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "processamento_job_id")
    private Long id;

    @Column(name = "processamento_job_code", unique = true, nullable = false, length = 6)
    private String code;

    @Column(name = "extrato_id", nullable = false)
    private Long extratoId;

    @Column(name = "extrato_code", nullable = false, length = 6)
    private String extratoCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
            value = {
                    @JoinColumn(name = "extrato_id", referencedColumnName = "extrato_id",
                            insertable = false, updatable = false),
                    @JoinColumn(name = "extrato_code", referencedColumnName = "extrato_code",
                            insertable = false, updatable = false)
            },
            foreignKey = @ForeignKey(name = "fk_processamento_jobs_extrato")
    )
    private ExtratoEntity extrato;

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
    private Long correlationId;

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
        entity.code = domain.getCode();
        entity.extratoId = domain.getExtratoId();
        entity.extratoCode = domain.getExtratoCode();
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
        return new ProcessamentoJob(id, code, extratoId, extratoCode, tipo, status, tentativas, maxTentativas,
                payload, erroMensagem, workerId, lockExpiresAt, correlationId,
                enfileiradoEm, iniciadoEm, concluidoEm, proximoRetry);
    }
}
