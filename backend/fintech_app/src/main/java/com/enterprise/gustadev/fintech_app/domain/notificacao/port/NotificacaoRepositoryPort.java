package com.enterprise.gustadev.fintech_app.domain.notificacao.port;

import com.enterprise.gustadev.fintech_app.domain.notificacao.model.Notificacao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificacaoRepositoryPort {
    Notificacao salvar(Notificacao notificacao);
    List<Notificacao> listarPorUsuario(UUID usuarioId);
    List<Notificacao> listarNaoEnviadas();
    Optional<Notificacao> buscarPorId(UUID id);
}
