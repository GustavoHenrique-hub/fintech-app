package com.enterprise.gustadev.fintech_app.application.notificacao.usecase;

import com.enterprise.gustadev.fintech_app.domain.notificacao.model.Notificacao;
import com.enterprise.gustadev.fintech_app.domain.notificacao.port.NotificacaoRepositoryPort;

import java.util.List;
import java.util.UUID;

public class ListarNotificacoesUseCase {

    private final NotificacaoRepositoryPort repository;

    public ListarNotificacoesUseCase(NotificacaoRepositoryPort repository) {
        this.repository = repository;
    }

    public List<Notificacao> executar(UUID usuarioId) {
        return repository.listarPorUsuario(usuarioId);
    }
}
