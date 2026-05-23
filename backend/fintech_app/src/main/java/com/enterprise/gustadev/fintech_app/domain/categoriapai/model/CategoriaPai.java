package com.enterprise.gustadev.fintech_app.domain.categoriapai.model;

import com.enterprise.gustadev.fintech_app.domain.categoriapai.exception.CategoriaPaiInvalidaException;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class CategoriaPai {

    private UUID id;
    private UUID categoriaId;
    private UUID paiId;
    private OffsetDateTime criadoEm;

    public CategoriaPai(UUID id, UUID categoriaId, UUID paiId, OffsetDateTime criadoEm) {
        this.id = id;
        this.categoriaId = categoriaId;
        this.paiId = paiId;
        this.criadoEm = criadoEm;
    }

    public CategoriaPai(UUID categoriaId, UUID paiId) {
        this(null, categoriaId, paiId, null);
    }

    public void validar() {
        if (categoriaId == null) {
            throw new CategoriaPaiInvalidaException("categoriaId é obrigatório");
        }
        if (paiId == null) {
            throw new CategoriaPaiInvalidaException("paiId é obrigatório");
        }
        if (categoriaId.equals(paiId)) {
            throw new CategoriaPaiInvalidaException("Uma categoria não pode ser pai de si mesma");
        }
    }
}
