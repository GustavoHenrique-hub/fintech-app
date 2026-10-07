package com.fintech;

public class ContaPoupanca extends ContaBancaria {

    private double taxaRendimentoMensal;

    public ContaPoupanca(String numero, Usuario titular, double saldoInicial, double taxaRendimentoMensal) {
        super(numero, titular, saldoInicial);
        setTaxaRendimentoMensal(taxaRendimentoMensal);
    }

    public double getTaxaRendimentoMensal() {
        return taxaRendimentoMensal;
    }

    public void setTaxaRendimentoMensal(double taxaRendimentoMensal) {
        if (taxaRendimentoMensal < 0) {
            throw new IllegalArgumentException("Taxa de rendimento nao pode ser negativa");
        }
        this.taxaRendimentoMensal = taxaRendimentoMensal;
    }

    @Override
    protected boolean podeSacar(double valor) {
        return getSaldo() >= valor;
    }

    @Override
    public double calcularRendimento() {
        return getSaldo() * taxaRendimentoMensal;
    }

    public void aplicarRendimento() {
        double rendimento = calcularRendimento();
        if (rendimento > 0) {
            creditar(rendimento);
        }
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" taxaRendimentoMensal=%.2f%%", taxaRendimentoMensal * 100);
    }
}
