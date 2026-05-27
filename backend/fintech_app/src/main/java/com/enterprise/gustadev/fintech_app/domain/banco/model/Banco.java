package com.enterprise.gustadev.fintech_app.domain.banco.model;

import com.enterprise.gustadev.fintech_app.domain.banco.exception.BancoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.shared.util.CodeGenerator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Banco {

    private Long id;
    private String code;
    private String nome;
    private String descricao;
    private String corHex;
    private String icone;

    public Banco(Long id, String code, String nome, String descricao, String corHex, String icone) {
        this.id = id;
        this.code = code;
        this.nome = nome;
        this.descricao = descricao;
        this.corHex = corHex;
        this.icone = icone;
    }

    public Banco(String nome, String descricao, String corHex, String icone) {
        this(null, CodeGenerator.gerar(), nome, descricao, corHex, icone);
    }

    public void validar() {
        if (nome == null || nome.isBlank()) {
            throw new BancoInvalidoException("Nome é obrigatório");
        }
    }
}
