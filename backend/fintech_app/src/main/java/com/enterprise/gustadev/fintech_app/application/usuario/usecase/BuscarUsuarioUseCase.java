package com.enterprise.gustadev.fintech_app.application.usuario.usecase;

import com.enterprise.gustadev.fintech_app.domain.usuario.exception.UsuarioInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;

public class BuscarUsuarioUseCase {

    private final UsuarioRepositoryPort repository;

    public BuscarUsuarioUseCase(UsuarioRepositoryPort repository) {
        this.repository = repository;
    }

    public Usuario executar(Long id) {
        if (id == null || id <= 0) {
            throw new UsuarioInvalidoException("Id do usuario invalido");
        }
        return repository.buscarPorId(id)
                .orElseThrow(() -> new UsuarioInvalidoException("Usuario nao encontrado"));
    }
}
