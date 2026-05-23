package com.enterprise.gustadev.fintech_app.application.categoria.usecase;

import com.enterprise.gustadev.fintech_app.domain.categoria.exception.CategoriaInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;

import java.util.UUID;

public class BuscarCategoriaUseCase {

    private final CategoriaRepositoryPort repository;

    public BuscarCategoriaUseCase(CategoriaRepositoryPort repository) {
        this.repository = repository;
    }

    public Categoria executar(UUID id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new CategoriaInvalidaException("Categoria não encontrada: " + id));
    }
}
