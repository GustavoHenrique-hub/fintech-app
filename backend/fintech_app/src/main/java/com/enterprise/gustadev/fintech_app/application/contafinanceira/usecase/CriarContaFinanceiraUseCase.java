package com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;

public class CriarContaFinanceiraUseCase {

    private final ContaFinanceiraRepositoryPort repository;

    public CriarContaFinanceiraUseCase(ContaFinanceiraRepositoryPort repository) {
        this.repository = repository;
    }

    public ContaFinanceira executar(ContaFinanceira conta) {
        conta.validar();
        conta.inicializarSaldo();
        return repository.salvar(conta);
    }
}
