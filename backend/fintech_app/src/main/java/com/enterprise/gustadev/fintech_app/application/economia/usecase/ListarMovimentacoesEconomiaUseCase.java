package com.enterprise.gustadev.fintech_app.application.economia.usecase;

import com.enterprise.gustadev.fintech_app.domain.economia.model.MovimentacaoEconomia;
import com.enterprise.gustadev.fintech_app.domain.economia.port.MovimentacaoEconomiaRepositoryPort;

import java.util.List;

public class ListarMovimentacoesEconomiaUseCase {

    private final MovimentacaoEconomiaRepositoryPort repository;

    public ListarMovimentacoesEconomiaUseCase(MovimentacaoEconomiaRepositoryPort repository) {
        this.repository = repository;
    }

    public List<MovimentacaoEconomia> executar(Long contaId) {
        return repository.listarPorConta(contaId);
    }
}
