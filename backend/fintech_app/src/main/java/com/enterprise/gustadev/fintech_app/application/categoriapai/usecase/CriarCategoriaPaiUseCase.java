package com.enterprise.gustadev.fintech_app.application.categoriapai.usecase;

import com.enterprise.gustadev.fintech_app.domain.categoriapai.model.CategoriaPai;
import com.enterprise.gustadev.fintech_app.domain.categoriapai.port.CategoriaPaiRepositoryPort;

public class CriarCategoriaPaiUseCase {

    private final CategoriaPaiRepositoryPort repository;

    public CriarCategoriaPaiUseCase(CategoriaPaiRepositoryPort repository) {
        this.repository = repository;
    }

    public CategoriaPai executar(CategoriaPai categoriaPai) {
        categoriaPai.validar();
        return repository.salvar(categoriaPai);
    }
}
