package com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;

import java.util.UUID;

public class DeletarContaFinanceiraUseCase {

    private final ContaFinanceiraRepositoryPort repository;

    public DeletarContaFinanceiraUseCase(ContaFinanceiraRepositoryPort repository) {
        this.repository = repository;
    }

    public void executar(UUID id, String code) {
        repository.buscarPorIdECode(id, code)
                .orElseThrow(() -> new ContaFinanceiraInvalidaException("Conta financeira não encontrada: " + id));
        repository.deletarPorId(id);
    }
}
