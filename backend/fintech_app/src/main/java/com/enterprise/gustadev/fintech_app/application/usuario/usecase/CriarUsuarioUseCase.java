package com.enterprise.gustadev.fintech_app.application.usuario.usecase;

import com.enterprise.gustadev.fintech_app.domain.auth.port.SenhaEncoder;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;

public class CriarUsuarioUseCase {

    private final UsuarioRepositoryPort repository;
    private final SenhaEncoder senhaEncoder;

    public CriarUsuarioUseCase(UsuarioRepositoryPort repository, SenhaEncoder senhaEncoder) {
        this.repository = repository;
        this.senhaEncoder = senhaEncoder;
    }

    public Usuario executar(Usuario usuario) {
        usuario.validar();
        usuario.setSenha(senhaEncoder.encode(usuario.getSenha()));
        return repository.salvar(usuario);
    }
}
