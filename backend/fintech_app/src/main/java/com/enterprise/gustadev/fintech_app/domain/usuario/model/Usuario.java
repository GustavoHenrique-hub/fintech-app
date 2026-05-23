package com.enterprise.gustadev.fintech_app.domain.usuario.model;

import com.enterprise.gustadev.fintech_app.domain.usuario.exception.UsuarioInvalidoException;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class Usuario {

    private Long idUsuario;
    private String usuarioCode;
    private String cpf;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private Long telegramChatID;
    private Long whatsappChatID;
    private Boolean emailVerificado;
    private LocalDate dtNascimento;

    public Usuario(Long idUsuario, String usuarioCode, String cpf, String nome, String email, String senha, String telefone, Long telegramChatID, Long whatsappChatID, Boolean emailVerificado, LocalDate dtNascimento) {
        this.idUsuario = idUsuario;
        this.usuarioCode = usuarioCode;
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.telegramChatID = telegramChatID;
        this.whatsappChatID = whatsappChatID;
        this.emailVerificado = emailVerificado;
        this.dtNascimento = dtNascimento;
    }

    public Usuario(String cpf, String nome, String email, String senha, String telefone, Long telegramChatID, Long whatsappChatID, Boolean emailVerificado, LocalDate dtNascimento) {
        this(null, null, cpf, nome, email, senha, telefone, telegramChatID, whatsappChatID, emailVerificado, dtNascimento);
    }

    public void validar() {
        if (cpf == null || cpf.isBlank()) {
            throw new UsuarioInvalidoException("CPF e obrigatorio");
        }
        if (nome == null || nome.isBlank()) {
            throw new UsuarioInvalidoException("Nome e obrigatorio");
        }
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new UsuarioInvalidoException("Email invalido");
        }
        if (senha == null || senha.isBlank()) {
            throw new UsuarioInvalidoException("Senha e obrigatoria");
        }
        if (dtNascimento == null) {
            throw new UsuarioInvalidoException("Data de nascimento e obrigatoria");
        }
    }

}
