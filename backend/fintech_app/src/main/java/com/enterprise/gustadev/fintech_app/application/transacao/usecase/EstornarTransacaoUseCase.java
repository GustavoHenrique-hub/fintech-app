package com.enterprise.gustadev.fintech_app.application.transacao.usecase;

import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;


public class EstornarTransacaoUseCase {

    private final TransacaoRepositoryPort repository;

    public EstornarTransacaoUseCase(TransacaoRepositoryPort repository) {
        this.repository = repository;
    }

    public Transacao executar(Long id, String code) {
        return repository.buscarPorIdECode(id, code)
                .orElseThrow(() -> new TransacaoInvalidaException("Transação não encontrada: " + id));
    }
}
