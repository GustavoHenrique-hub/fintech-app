package com.enterprise.gustadev.fintech_app.application.gasto.usecase;

import com.enterprise.gustadev.fintech_app.domain.gasto.exception.GastoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.gasto.port.GastoRepositoryPort;

public class DeletarGastoUseCase {

    private final GastoRepositoryPort repository;

    public DeletarGastoUseCase(GastoRepositoryPort repository) {
        this.repository = repository;
    }

    public void executar(Long id) {
        if (id == null || id <= 0) {
            throw new GastoInvalidoException("Id do gasto invalido");
        }
        repository.deletarPorId(id);
    }
}
