package com.enterprise.gustadev.fintech_app.adapters.out.persistence.consentimentolgpd;

import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.model.ConsentimentoLgpd;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConsentimentoLgpd;
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
@Table(name = "consentimentos_lgpd")
@Getter
@Setter
@NoArgsConstructor
public class ConsentimentoLgpdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoConsentimentoLgpd tipo;

    @Column(name = "versao_politica", nullable = false, length = 20)
    private String versaoPolitica;

    @Column(nullable = false)
    private boolean consentido;

    @Column(name = "ip_origem", length = 45)
    private String ipOrigem;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    @Column(name = "revogado_em")
    private OffsetDateTime revogadoEm;

    @Column(name = "revogado_motivo", columnDefinition = "TEXT")
    private String revogadoMotivo;

    public static ConsentimentoLgpdEntity fromDomain(ConsentimentoLgpd domain) {
        ConsentimentoLgpdEntity entity = new ConsentimentoLgpdEntity();
        entity.id = domain.getId();
        entity.usuarioId = domain.getUsuarioId();
        entity.tipo = domain.getTipo();
        entity.versaoPolitica = domain.getVersaoPolitica();
        entity.consentido = domain.isConsentido();
        entity.ipOrigem = domain.getIpOrigem();
        entity.criadoEm = domain.getCriadoEm();
        entity.revogadoEm = domain.getRevogadoEm();
        entity.revogadoMotivo = domain.getRevogadoMotivo();
        return entity;
    }

    public ConsentimentoLgpd toDomain() {
        return new ConsentimentoLgpd(id, usuarioId, tipo, versaoPolitica, consentido,
                ipOrigem, criadoEm, revogadoEm, revogadoMotivo);
    }
}
