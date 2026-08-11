package com.enterprise.gustadev.fintech_app.adapters.out.persistence.consentimentolgpd;

import com.enterprise.gustadev.fintech_app.adapters.out.persistence.usuario.UsuarioEntity;
import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.model.ConsentimentoLgpd;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConsentimentoLgpd;
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
@Table(name = "consentimentos_lgpd")
@Getter
@Setter
@NoArgsConstructor
public class ConsentimentoLgpdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consentimento_lgpd_id")
    private Long id;

    @Column(name = "consentimento_lgpd_code", unique = true, nullable = false, length = 6)
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
            foreignKey = @ForeignKey(name = "fk_consentimentos_lgpd_usuario")
    )
    private UsuarioEntity usuario;

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
        entity.code = domain.getCode();
        entity.usuarioId = domain.getUsuarioId();
        entity.usuarioCode = domain.getUsuarioCode();
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
        return new ConsentimentoLgpd(id, code, usuarioId, usuarioCode, tipo, versaoPolitica, consentido,
                ipOrigem, criadoEm, revogadoEm, revogadoMotivo);
    }
}
