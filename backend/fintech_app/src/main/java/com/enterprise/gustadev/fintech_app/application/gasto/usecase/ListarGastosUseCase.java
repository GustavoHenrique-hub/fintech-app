package com.enterprise.gustadev.fintech_app.application.gasto.usecase;

import com.enterprise.gustadev.fintech_app.domain.gasto.model.Gasto;
import com.enterprise.gustadev.fintech_app.domain.gasto.port.GastoRepositoryPort;

import java.util.List;

public class ListarGastosUseCase {

    private final GastoRepositoryPort repository;

    public ListarGastosUseCase(GastoRepositoryPort repository) {
        this.repository = repository;
    }

    public List<Gasto> executar(Long usuarioId) {
        return repository.listarPorUsuario(usuarioId);
    }
}
