package com.enterprise.gustadev.fintech_app.adapters.out.persistence.auditoriaevento;

import com.enterprise.gustadev.fintech_app.domain.auditoriaevento.model.AuditoriaEvento;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.AcaoAuditoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemAuditoria;
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

@Entity
@Table(name = "auditoria_eventos")
@Getter
@Setter
@NoArgsConstructor
public class AuditoriaEventoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "correlation_id", nullable = false)
    private Long correlationId;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(nullable = false, length = 50)
    private String entidade;

    @Column(name = "entidade_id", nullable = false)
    private Long entidadeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AcaoAuditoria acao;

    @Column(name = "dados_anteriores", columnDefinition = "TEXT")
    private String dadosAnteriores;

    @Column(name = "dados_novos", columnDefinition = "TEXT")
    private String dadosNovos;

    @Column(name = "ip_origem", length = 45)
    private String ipOrigem;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private OrigemAuditoria origem;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    public static AuditoriaEventoEntity fromDomain(AuditoriaEvento domain) {
        AuditoriaEventoEntity entity = new AuditoriaEventoEntity();
        entity.id = domain.getId();
        entity.correlationId = domain.getCorrelationId();
        entity.usuarioId = domain.getUsuarioId();
        entity.entidade = domain.getEntidade();
        entity.entidadeId = domain.getEntidadeId();
        entity.acao = domain.getAcao();
        entity.dadosAnteriores = domain.getDadosAnteriores();
        entity.dadosNovos = domain.getDadosNovos();
        entity.ipOrigem = domain.getIpOrigem();
        entity.userAgent = domain.getUserAgent();
        entity.origem = domain.getOrigem();
        entity.criadoEm = domain.getCriadoEm();
        return entity;
    }

    public AuditoriaEvento toDomain() {
        return new AuditoriaEvento(id, correlationId, usuarioId, entidade, entidadeId,
                acao, dadosAnteriores, dadosNovos, ipOrigem, userAgent, origem, criadoEm);
    }
}
