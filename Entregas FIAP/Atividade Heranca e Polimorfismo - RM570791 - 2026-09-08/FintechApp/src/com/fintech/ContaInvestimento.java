package com.fintech;

public class ContaInvestimento extends ContaBancaria {

    private double taxaRendimentoMensal;
    private double aliquotaImposto;

    public ContaInvestimento(String numero, Usuario titular, double saldoInicial,
                              double taxaRendimentoMensal, double aliquotaImposto) {
        super(numero, titular, saldoInicial);
        setTaxaRendimentoMensal(taxaRendimentoMensal);
        setAliquotaImposto(aliquotaImposto);
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

    public double getAliquotaImposto() {
        return aliquotaImposto;
    }

    public void setAliquotaImposto(double aliquotaImposto) {
        if (aliquotaImposto < 0 || aliquotaImposto > 1) {
            throw new IllegalArgumentException("Aliquota de imposto deve estar entre 0 e 1");
        }
        this.aliquotaImposto = aliquotaImposto;
    }

    @Override
    protected boolean podeSacar(double valor) {
        return getSaldo() >= valor;
    }

    @Override
    public double calcularRendimento() {
        double rendimentoBruto = getSaldo() * taxaRendimentoMensal;
        return rendimentoBruto * (1 - aliquotaImposto);
    }

    public void aplicarRendimento() {
        double rendimento = calcularRendimento();
        if (rendimento > 0) {
            creditar(rendimento);
        }
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" taxaRendimentoMensal=%.2f%% aliquotaImposto=%.0f%%",
                taxaRendimentoMensal * 100, aliquotaImposto * 100);
    }
}
