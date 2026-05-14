package com.enterprise.gustadev.fintech_app.domain.usuario.model;

import com.enterprise.gustadev.fintech_app.domain.usuario.exception.UsuarioInvalidoException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class Usuario {

    private Long id;
    private String usercode;
    private String cpf;
    private String rg;
    private String nome;
    private String sobrenome;
    private String email;
    private String senha;
    private LocalDate dataNascimento;

    public Usuario(Long id, String usercode, String cpf, String rg, String nome, String sobrenome, String email, String senha, LocalDate dataNascimento) {
        this.id = id;
        this.usercode = usercode;
        this.cpf = cpf;
        this.rg = rg;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
    }

    public Usuario(String usercode, String cpf, String rg, String nome, String sobrenome, String email, String senha, LocalDate dataNascimento) {
        this(null, usercode, cpf, rg, nome, sobrenome, email, senha, dataNascimento);
    }

    public void validar() {
        if (cpf == null || cpf.isBlank()) {
            throw new UsuarioInvalidoException("CPF e obrigatorio");
        }
        if (rg == null || rg.isBlank()) {
            throw new UsuarioInvalidoException("RG e obrigatorio");
        }
        if (nome == null || nome.isBlank()) {
            throw new UsuarioInvalidoException("Nome e obrigatorio");
        }
        if (sobrenome == null || sobrenome.isBlank()) {
            throw new UsuarioInvalidoException("Sobrenome e obrigatorio");
        }
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new UsuarioInvalidoException("Email invalido");
        }
        if (senha == null || senha.isBlank()) {
            throw new UsuarioInvalidoException("Senha e obrigatoria");
        }
        if (dataNascimento == null) {
            throw new UsuarioInvalidoException("Data de nascimento e obrigatoria");
        }
    }

}
