package com.enterprise.gustadev.fintech_app.application.notificacao.usecase;

import com.enterprise.gustadev.fintech_app.domain.notificacao.model.Notificacao;
import com.enterprise.gustadev.fintech_app.domain.notificacao.port.NotificacaoRepositoryPort;

public class CriarNotificacaoUseCase {

    private final NotificacaoRepositoryPort repository;

    public CriarNotificacaoUseCase(NotificacaoRepositoryPort repository) {
        this.repository = repository;
    }

    public Notificacao executar(Notificacao notificacao) {
        return repository.salvar(notificacao);
    }
}
