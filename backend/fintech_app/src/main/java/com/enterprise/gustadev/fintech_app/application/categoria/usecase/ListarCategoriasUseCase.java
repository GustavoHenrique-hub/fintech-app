package com.enterprise.gustadev.fintech_app.application.categoria.usecase;

import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;

import java.util.List;
import java.util.UUID;

public class ListarCategoriasUseCase {

    private final CategoriaRepositoryPort repository;

    public ListarCategoriasUseCase(CategoriaRepositoryPort repository) {
        this.repository = repository;
    }

    public List<Categoria> executarPadrao() {
        return repository.listarPadrao();
    }

    public List<Categoria> executarPorUsuario(UUID usuarioId) {
        return repository.listarPorUsuario(usuarioId);
    }
}
