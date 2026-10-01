package com.enterprise.gustadev.fintech_app.application.usuario.usecase;

import com.enterprise.gustadev.fintech_app.domain.usuario.exception.UsuarioInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;

public class DesvincularTelegramUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    public DesvincularTelegramUseCase(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void executar(Long usuarioId) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new UsuarioInvalidoException("Usuario nao encontrado"));
        if (usuario.getTelegramChatID() == null) {
            return;
        }
        usuario.setTelegramChatID(null);
        usuarioRepository.salvar(usuario);
    }
}
