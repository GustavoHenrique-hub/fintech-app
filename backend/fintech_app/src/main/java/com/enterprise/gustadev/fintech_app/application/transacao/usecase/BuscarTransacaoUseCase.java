package com.enterprise.gustadev.fintech_app.application.transacao.usecase;

import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;

import java.util.UUID;

public class BuscarTransacaoUseCase {

    private final TransacaoRepositoryPort repository;

    public BuscarTransacaoUseCase(TransacaoRepositoryPort repository) {
        this.repository = repository;
    }

    public Transacao executar(UUID id, String code) {
        return repository.buscarPorIdECode(id, code)
                .orElseThrow(() -> new TransacaoInvalidaException("Transação não encontrada: " + id));
    }
}
