package com.enterprise.gustadev.fintech_app.application.usuario.usecase;

import com.enterprise.gustadev.fintech_app.domain.auth.exception.CredenciaisInvalidasException;
import com.enterprise.gustadev.fintech_app.domain.auth.port.SenhaEncoder;
import com.enterprise.gustadev.fintech_app.domain.usuario.exception.UsuarioInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;

public class AlterarSenhaUseCase {

    private final UsuarioRepositoryPort repository;
    private final SenhaEncoder senhaEncoder;

    public AlterarSenhaUseCase(UsuarioRepositoryPort repository, SenhaEncoder senhaEncoder) {
        this.repository = repository;
        this.senhaEncoder = senhaEncoder;
    }

    public Usuario executar(Long id, String senhaAtual, String novaSenha) {
        Usuario usuario = repository.buscarPorId(id)
                .orElseThrow(() -> new UsuarioInvalidoException("Usuario nao encontrado"));

        if (!senhaEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new CredenciaisInvalidasException("Senha atual incorreta");
        }
        if (novaSenha == null || novaSenha.isBlank()) {
            throw new UsuarioInvalidoException("Nova senha e obrigatoria");
        }

        usuario.setSenha(senhaEncoder.encode(novaSenha));
        return repository.salvar(usuario);
    }
}
