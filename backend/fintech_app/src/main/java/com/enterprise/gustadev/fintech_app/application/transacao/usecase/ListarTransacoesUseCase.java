package com.enterprise.gustadev.fintech_app.application.transacao.usecase;

import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;

import java.util.List;

public class ListarTransacoesUseCase {

    private final TransacaoRepositoryPort repository;

    public ListarTransacoesUseCase(TransacaoRepositoryPort repository) {
        this.repository = repository;
    }

    public List<Transacao> executarPorUsuario(Long usuarioId) {
        return repository.listarPorUsuario(usuarioId);
    }

    public List<Transacao> executarPorConta(Long contaId) {
        return repository.listarPorConta(contaId);
    }

    public List<Transacao> executarPorExtrato(Long extratoId) {
        return repository.listarPorExtrato(extratoId);
    }
}
