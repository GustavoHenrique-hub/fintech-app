package com.fintech;

import java.util.ArrayList;
import java.util.List;

public abstract class ContaBancaria {

    private final String numero;
    private final Usuario titular;
    private double saldo;
    private final List<Transacao> historico = new ArrayList<>();

    protected ContaBancaria(String numero, Usuario titular, double saldoInicial) {
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("Numero da conta e obrigatorio");
        }
        if (titular == null) {
            throw new IllegalArgumentException("Titular e obrigatorio");
        }
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("Saldo inicial nao pode ser negativo");
        }
        this.numero = numero;
        this.titular = titular;
        this.saldo = saldoInicial;
    }

    public String getNumero() {
        return numero;
    }

    public Usuario getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public List<Transacao> getHistorico() {
        return List.copyOf(historico);
    }

    public void depositar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor do deposito deve ser positivo");
        }
        saldo += valor;
        historico.add(new Transacao(TipoTransacao.DEPOSITO, valor, "Deposito em " + numero));
    }

    public final void sacar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor do saque deve ser positivo");
        }
        if (!podeSacar(valor)) {
            throw new SaldoInsuficienteException(
                    String.format("Saldo insuficiente para saque de R$ %.2f na conta %s", valor, numero));
        }
        saldo -= valor;
        historico.add(new Transacao(TipoTransacao.SAQUE, valor, "Saque em " + numero));
    }

    protected void creditar(double valor) {
        saldo += valor;
    }

    /** Regra de saque de cada tipo de conta (ex.: cheque especial, carencia). */
    protected abstract boolean podeSacar(double valor);

    /** Rendimento do periodo, calculado de forma diferente por cada subclasse. */
    public abstract double calcularRendimento();

    public String exibirExtrato() {
        StringBuilder sb = new StringBuilder();
        sb.append(this).append(System.lineSeparator());
        for (Transacao transacao : historico) {
            sb.append("  ").append(transacao.getResumo()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("%s numero=%s titular=%s saldo=R$ %.2f",
                getClass().getSimpleName(), numero, titular.getNome(), saldo);
    }
}
