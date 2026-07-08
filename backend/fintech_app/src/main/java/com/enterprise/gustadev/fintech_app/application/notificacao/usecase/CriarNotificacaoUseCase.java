package com.enterprise.gustadev.fintech_app.application.notificacao.usecase;

import com.enterprise.gustadev.fintech_app.domain.notificacao.model.Notificacao;
import com.enterprise.gustadev.fintech_app.domain.notificacao.port.NotificacaoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;

public class CriarNotificacaoUseCase {

    private final NotificacaoRepositoryPort repository;
    private final UsuarioRepositoryPort usuarioRepository;

    public CriarNotificacaoUseCase(NotificacaoRepositoryPort repository,
                                   UsuarioRepositoryPort usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public Notificacao executar(Notificacao notificacao) {
        if (notificacao.getUsuarioCode() == null || notificacao.getUsuarioCode().isBlank()) {
            Usuario usuario = usuarioRepository.buscarPorId(notificacao.getUsuarioId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Usuário não encontrado: " + notificacao.getUsuarioId()));
            notificacao.setUsuarioCode(usuario.getUsuarioCode());
        }
        return repository.salvar(notificacao);
    }
}
