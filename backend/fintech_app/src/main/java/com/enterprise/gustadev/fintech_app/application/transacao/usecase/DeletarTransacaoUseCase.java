package com.enterprise.gustadev.fintech_app.application.transacao.usecase;

import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;

import java.util.UUID;

public class DeletarTransacaoUseCase {

    private final TransacaoRepositoryPort repository;

    public DeletarTransacaoUseCase(TransacaoRepositoryPort repository) {
        this.repository = repository;
    }

    public void executar(UUID id, String code) {
        repository.buscarPorIdECode(id, code)
                .orElseThrow(() -> new TransacaoInvalidaException("Transação não encontrada: " + id));
        repository.deletarPorId(id);
    }
}
