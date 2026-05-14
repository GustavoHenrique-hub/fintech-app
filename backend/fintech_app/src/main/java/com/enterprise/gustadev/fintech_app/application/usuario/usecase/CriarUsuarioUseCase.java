package com.enterprise.gustadev.fintech_app.application.usuario.usecase;

import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;

public class CriarUsuarioUseCase {

    private final UsuarioRepositoryPort repository;

    public CriarUsuarioUseCase(UsuarioRepositoryPort repository) {
        this.repository = repository;
    }

    public Usuario executar(Usuario usuario) {
        usuario.validar();
        return repository.salvar(usuario);
    }
}
