package com.enterprise.gustadev.fintech_app.application.categoria.usecase;

import com.enterprise.gustadev.fintech_app.domain.categoria.exception.CategoriaInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;


public class BuscarCategoriaUseCase {

    private final CategoriaRepositoryPort repository;

    public BuscarCategoriaUseCase(CategoriaRepositoryPort repository) {
        this.repository = repository;
    }

    public Categoria executar(Long id, String code) {
        return repository.buscarPorIdECode(id, code)
                .orElseThrow(() -> new CategoriaInvalidaException("Categoria não encontrada: " + id));
    }
}
