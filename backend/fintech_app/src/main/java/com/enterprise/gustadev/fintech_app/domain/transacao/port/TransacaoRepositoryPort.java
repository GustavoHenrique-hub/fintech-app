package com.enterprise.gustadev.fintech_app.domain.transacao.port;

import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;

import java.util.List;
import java.util.Optional;

public interface TransacaoRepositoryPort {
    Transacao salvar(Transacao transacao);
    List<Transacao> listarPorUsuario(Long usuarioId);
    List<Transacao> listarPorConta(Long contaId);
    List<Transacao> listarPorExtrato(Long extratoId);
    Optional<Transacao> buscarPorId(Long id);
    Optional<Transacao> buscarPorIdECode(Long id, String code);
    void deletarPorId(Long id);
}
