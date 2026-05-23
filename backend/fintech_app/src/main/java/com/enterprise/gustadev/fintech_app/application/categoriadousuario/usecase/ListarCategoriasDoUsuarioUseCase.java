package com.enterprise.gustadev.fintech_app.application.categoriadousuario.usecase;

import com.enterprise.gustadev.fintech_app.domain.categoriadousuario.model.CategoriaDoUsuario;
import com.enterprise.gustadev.fintech_app.domain.categoriadousuario.port.CategoriaDoUsuarioRepositoryPort;

import java.util.List;
import java.util.UUID;

public class ListarCategoriasDoUsuarioUseCase {

    private final CategoriaDoUsuarioRepositoryPort repository;

    public ListarCategoriasDoUsuarioUseCase(CategoriaDoUsuarioRepositoryPort repository) {
        this.repository = repository;
    }

    public List<CategoriaDoUsuario> executar(UUID usuarioId) {
        return repository.listarPorUsuario(usuarioId);
    }
}
