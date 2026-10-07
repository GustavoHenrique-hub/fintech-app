package com.fintech;

import java.time.LocalDate;

public class Usuario {

    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private final LocalDate dataCadastro;

    public Usuario(String nome, String cpf, String email, String senha) {
        setNome(nome);
        setCpf(cpf);
        setEmail(email);
        setSenha(senha);
        this.dataCadastro = LocalDate.now();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome e obrigatorio");
        }
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF e obrigatorio");
        }
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("E-mail invalido");
        }
        this.email = email;
    }

    private void setSenha(String senha) {
        if (senha == null || senha.length() < 6) {
            throw new IllegalArgumentException("Senha deve ter ao menos 6 caracteres");
        }
        this.senha = senha;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public boolean autenticar(String senhaDigitada) {
        return senha != null && senha.equals(senhaDigitada);
    }

    public void alterarSenha(String senhaAtual, String novaSenha) {
        if (!autenticar(senhaAtual)) {
            throw new IllegalStateException("Senha atual incorreta");
        }
        setSenha(novaSenha);
    }

    @Override
    public String toString() {
        return String.format("Usuario{nome='%s', cpf='%s', email='%s'}", nome, cpf, email);
    }
}
