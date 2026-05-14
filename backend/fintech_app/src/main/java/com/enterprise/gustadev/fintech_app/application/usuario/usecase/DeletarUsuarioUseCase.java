package com.enterprise.gustadev.fintech_app.application.usuario.usecase;

import com.enterprise.gustadev.fintech_app.domain.usuario.exception.UsuarioInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;

public class DeletarUsuarioUseCase {

    private final UsuarioRepositoryPort repository;

    public DeletarUsuarioUseCase(UsuarioRepositoryPort repository) {
        this.repository = repository;
    }

    public void executar(Long id) {
        if (id == null || id <= 0) {
            throw new UsuarioInvalidoException("Id do usuario invalido");
        }
        repository.deletarPorId(id);
    }
}
