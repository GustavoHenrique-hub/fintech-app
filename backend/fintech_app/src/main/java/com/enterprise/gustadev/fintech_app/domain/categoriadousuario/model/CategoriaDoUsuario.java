package com.enterprise.gustadev.fintech_app.domain.categoriadousuario.model;

import com.enterprise.gustadev.fintech_app.domain.categoriadousuario.exception.CategoriaDoUsuarioInvalidaException;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class CategoriaDoUsuario {

    private Long id;
    private Long usuarioId;
    private Long categoriaId;
    private boolean ativa;
    private OffsetDateTime criadoEm;

    public CategoriaDoUsuario(Long id, Long usuarioId, Long categoriaId,
                               boolean ativa, OffsetDateTime criadoEm) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
        this.ativa = ativa;
        this.criadoEm = criadoEm;
    }

    public CategoriaDoUsuario(Long usuarioId, Long categoriaId) {
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
