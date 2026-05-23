package com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;

import java.util.UUID;

public class BuscarContaFinanceiraUseCase {

    private final ContaFinanceiraRepositoryPort repository;

    public BuscarContaFinanceiraUseCase(ContaFinanceiraRepositoryPort repository) {
        this.repository = repository;
    }

    public ContaFinanceira executar(UUID id, String code) {
        return repository.buscarPorIdECode(id, code)
                .orElseThrow(() -> new ContaFinanceiraInvalidaException("Conta financeira não encontrada: " + id));
    }
}
