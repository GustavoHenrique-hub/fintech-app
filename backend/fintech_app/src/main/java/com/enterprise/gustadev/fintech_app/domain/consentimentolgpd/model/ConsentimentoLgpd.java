package com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.model;

import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.exception.ConsentimentoLgpdInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConsentimentoLgpd;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ConsentimentoLgpd {

    private Long id;
    private Long usuarioId;
    private TipoConsentimentoLgpd tipo;
    private String versaoPolitica;
    private boolean consentido;
    private String ipOrigem;
    private OffsetDateTime criadoEm;
    private OffsetDateTime revogadoEm;
    private String revogadoMotivo;

    public ConsentimentoLgpd(Long id, Long usuarioId, TipoConsentimentoLgpd tipo,
                              String versaoPolitica, boolean consentido, String ipOrigem,
                              OffsetDateTime criadoEm, OffsetDateTime revogadoEm,
                              String revogadoMotivo) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.versaoPolitica = versaoPolitica;
        this.consentido = consentido;
        this.ipOrigem = ipOrigem;
        this.criadoEm = criadoEm;
        this.revogadoEm = revogadoEm;
        this.revogadoMotivo = revogadoMotivo;
    }

    public ConsentimentoLgpd(Long usuarioId, TipoConsentimentoLgpd tipo,
                              String versaoPolitica, boolean consentido, String ipOrigem) {
        this(null, usuarioId, tipo, versaoPolitica, consentido, ipOrigem, null, null, null);
    }

    public void validar() {
        if (usuarioId == null) {
            throw new ConsentimentoLgpdInvalidoException("UsuarioId é obrigatório");
        }
        if (tipo == null) {
            throw new ConsentimentoLgpdInvalidoException("Tipo é obrigatório");
        }
        if (versaoPolitica == null || versaoPolitica.isBlank()) {
            throw new ConsentimentoLgpdInvalidoException("Versão da política é obrigatória");
        }
    }
}
