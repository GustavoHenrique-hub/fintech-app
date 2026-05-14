package com.enterprise.gustadev.fintech_app.application.gasto.usecase;

import com.enterprise.gustadev.fintech_app.domain.gasto.model.Gasto;
import com.enterprise.gustadev.fintech_app.domain.gasto.port.GastoRepositoryPort;

public class CriarGastoUseCase {

    private final GastoRepositoryPort repository;

    public CriarGastoUseCase(GastoRepositoryPort repository) {
        this.repository = repository;
    }

    public Gasto executar(Gasto gasto) {
        gasto.validar();
        return repository.salvar(gasto);
    }
}
