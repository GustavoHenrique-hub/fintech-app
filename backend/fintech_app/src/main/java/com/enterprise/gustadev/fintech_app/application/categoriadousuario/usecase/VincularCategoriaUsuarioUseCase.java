package com.enterprise.gustadev.fintech_app.application.categoriadousuario.usecase;

import com.enterprise.gustadev.fintech_app.domain.categoriadousuario.model.CategoriaDoUsuario;
import com.enterprise.gustadev.fintech_app.domain.categoriadousuario.port.CategoriaDoUsuarioRepositoryPort;

public class VincularCategoriaUsuarioUseCase {

    private final CategoriaDoUsuarioRepositoryPort repository;

    public VincularCategoriaUsuarioUseCase(CategoriaDoUsuarioRepositoryPort repository) {
        this.repository = repository;
    }

    public CategoriaDoUsuario executar(CategoriaDoUsuario categoriaDoUsuario) {
        categoriaDoUsuario.validar();
        return repository.salvar(categoriaDoUsuario);
    }
}
