package com.fintech;

public class ContaCorrente extends ContaBancaria {

    private double limiteChequeEspecial;

    public ContaCorrente(String numero, Usuario titular, double saldoInicial, double limiteChequeEspecial) {
        super(numero, titular, saldoInicial);
        setLimiteChequeEspecial(limiteChequeEspecial);
    }

    public double getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }

    public void setLimiteChequeEspecial(double limiteChequeEspecial) {
        if (limiteChequeEspecial < 0) {
            throw new IllegalArgumentException("Limite nao pode ser negativo");
        }
        this.limiteChequeEspecial = limiteChequeEspecial;
    }

    @Override
    protected boolean podeSacar(double valor) {
        return getSaldo() - valor >= -limiteChequeEspecial;
    }

    @Override
    public double calcularRendimento() {
        return 0.0;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" limiteChequeEspecial=R$ %.2f", limiteChequeEspecial);
    }
}
