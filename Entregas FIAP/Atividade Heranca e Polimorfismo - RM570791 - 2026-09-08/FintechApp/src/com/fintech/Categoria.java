package com.fintech;

public class Categoria {

    private String nome;
    private TipoCategoria tipo;

    public Categoria(String nome, TipoCategoria tipo) {
        setNome(nome);
        setTipo(tipo);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da categoria e obrigatorio");
        }
        this.nome = nome;
    }

    public TipoCategoria getTipo() {
        return tipo;
    }

    public void setTipo(TipoCategoria tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo da categoria e obrigatorio");
        }
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", nome, tipo);
    }
}
