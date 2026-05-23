package com.enterprise.gustadev.fintech_app.domain.transacao.port;

import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransacaoRepositoryPort {
    Transacao salvar(Transacao transacao);
    List<Transacao> listarPorUsuario(UUID usuarioId);
    List<Transacao> listarPorConta(UUID contaId);
    List<Transacao> listarPorExtrato(UUID extratoId);
    Optional<Transacao> buscarPorId(UUID id);
    void deletarPorId(UUID id);
}
