package com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;

import java.util.List;
import java.util.UUID;

public class ListarContasFinanceirasUseCase {

    private final ContaFinanceiraRepositoryPort repository;

    public ListarContasFinanceirasUseCase(ContaFinanceiraRepositoryPort repository) {
        this.repository = repository;
    }

    public List<ContaFinanceira> executar(UUID usuarioId) {
        return repository.listarPorUsuario(usuarioId);
    }
}
