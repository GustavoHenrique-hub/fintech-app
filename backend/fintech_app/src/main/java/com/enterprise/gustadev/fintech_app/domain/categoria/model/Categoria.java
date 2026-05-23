package com.enterprise.gustadev.fintech_app.domain.categoria.model;

import com.enterprise.gustadev.fintech_app.domain.categoria.exception.CategoriaInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class Categoria {

    private UUID id;
    private String nome;
    private TipoCategoria tipo;
    private String icone;
    private String corHex;
    private boolean padrao;
    private OffsetDateTime criadoEm;

    public Categoria(UUID id, String nome, TipoCategoria tipo, String icone,
                     String corHex, boolean padrao, OffsetDateTime criadoEm) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.icone = icone;
        this.corHex = corHex;
        this.padrao = padrao;
        this.criadoEm = criadoEm;
    }

    public Categoria(String nome, TipoCategoria tipo, String icone, String corHex) {
        this(null, nome, tipo, icone, corHex, false, null);
    }

    public void validar() {
        if (nome == null || nome.isBlank()) {
            throw new CategoriaInvalidaException("Nome é obrigatório");
        }
        if (tipo == null) {
            throw new CategoriaInvalidaException("Tipo é obrigatório");
        }
    }
}
