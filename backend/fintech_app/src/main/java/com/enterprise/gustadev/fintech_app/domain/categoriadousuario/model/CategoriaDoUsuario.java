package com.enterprise.gustadev.fintech_app.domain.categoriadousuario.model;

import com.enterprise.gustadev.fintech_app.domain.categoriadousuario.exception.CategoriaDoUsuarioInvalidaException;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class CategoriaDoUsuario {

    private UUID id;
    private UUID usuarioId;
    private UUID categoriaId;
    private boolean ativa;
    private OffsetDateTime criadoEm;

    public CategoriaDoUsuario(UUID id, UUID usuarioId, UUID categoriaId,
                               boolean ativa, OffsetDateTime criadoEm) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
        this.ativa = ativa;
        this.criadoEm = criadoEm;
    }

    public CategoriaDoUsuario(UUID usuarioId, UUID categoriaId) {
        this(null, usuarioId, categoriaId, true, null);
    }

    public void validar() {
        if (usuarioId == null) {
            throw new CategoriaDoUsuarioInvalidaException("usuarioId é obrigatório");
        }
        if (categoriaId == null) {
            throw new CategoriaDoUsuarioInvalidaException("categoriaId é obrigatório");
        }
    }
}
