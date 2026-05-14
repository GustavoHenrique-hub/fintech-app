package com.enterprise.gustadev.fintech_app.application.usuario.usecase;

import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;

import java.util.List;

public class ListarUsuariosUseCase {

    private final UsuarioRepositoryPort repository;

    public ListarUsuariosUseCase(UsuarioRepositoryPort repository) {
        this.repository = repository;
    }

    public List<Usuario> executar() {
        return repository.listarTodos();
    }
}
