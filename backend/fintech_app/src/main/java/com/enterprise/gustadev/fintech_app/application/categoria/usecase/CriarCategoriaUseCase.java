package com.enterprise.gustadev.fintech_app.application.categoria.usecase;

import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;

public class CriarCategoriaUseCase {

    private final CategoriaRepositoryPort repository;

    public CriarCategoriaUseCase(CategoriaRepositoryPort repository) {
        this.repository = repository;
    }

    public Categoria executar(Categoria categoria) {
        categoria.validar();
        return repository.salvar(categoria);
    }
}
