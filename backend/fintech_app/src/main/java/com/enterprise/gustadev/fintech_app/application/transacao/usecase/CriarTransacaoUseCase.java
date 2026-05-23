package com.enterprise.gustadev.fintech_app.application.transacao.usecase;

import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;

public class CriarTransacaoUseCase {

    private final TransacaoRepositoryPort repository;

    public CriarTransacaoUseCase(TransacaoRepositoryPort repository) {
        this.repository = repository;
    }

    public Transacao executar(Transacao transacao) {
        transacao.validar();
        return repository.salvar(transacao);
    }
}
