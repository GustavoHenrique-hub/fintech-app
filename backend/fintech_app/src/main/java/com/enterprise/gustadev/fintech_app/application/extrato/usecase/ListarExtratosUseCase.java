package com.enterprise.gustadev.fintech_app.application.extrato.usecase;

import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;

import java.util.List;
import java.util.UUID;

public class ListarExtratosUseCase {

    private final ExtratoRepositoryPort repository;

    public ListarExtratosUseCase(ExtratoRepositoryPort repository) {
        this.repository = repository;
    }

    public List<Extrato> executar(UUID usuarioId) {
        return repository.listarPorUsuario(usuarioId);
    }
}
