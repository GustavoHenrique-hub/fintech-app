package com.fintech;

import java.time.LocalDateTime;

public class Transacao {

    private static int proximoId = 1;

    private final int id;
    private final TipoTransacao tipo;
    private final double valor;
    private final LocalDateTime dataHora;
    private final String descricao;

    public Transacao(TipoTransacao tipo, double valor, String descricao) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo da transacao e obrigatorio");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor da transacao deve ser positivo");
        }
        this.id = proximoId++;
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.dataHora = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getResumo() {
        return String.format("#%d [%s] %s - R$ %.2f", id, tipo, descricao, valor);
    }

    @Override
    public String toString() {
        return getResumo();
    }
}
