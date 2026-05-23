package com.enterprise.gustadev.fintech_app.application.categoriapai.usecase;

import com.enterprise.gustadev.fintech_app.domain.categoriapai.model.CategoriaPai;
import com.enterprise.gustadev.fintech_app.domain.categoriapai.port.CategoriaPaiRepositoryPort;

import java.util.List;

public class ListarCategoriasPaiUseCase {

    private final CategoriaPaiRepositoryPort repository;

    public ListarCategoriasPaiUseCase(CategoriaPaiRepositoryPort repository) {
        this.repository = repository;
    }

    public List<CategoriaPai> executar(Long paiId) {
        return repository.listarPorPai(paiId);
    }
}
