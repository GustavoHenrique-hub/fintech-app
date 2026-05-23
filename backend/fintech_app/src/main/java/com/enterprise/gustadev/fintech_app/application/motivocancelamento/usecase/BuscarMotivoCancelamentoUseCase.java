package com.enterprise.gustadev.fintech_app.application.motivocancelamento.usecase;

import com.enterprise.gustadev.fintech_app.domain.motivocancelamento.exception.MotivoCancelamentoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.motivocancelamento.model.MotivoCancelamento;
import com.enterprise.gustadev.fintech_app.domain.motivocancelamento.port.MotivoCancelamentoRepositoryPort;


public class BuscarMotivoCancelamentoUseCase {

    private final MotivoCancelamentoRepositoryPort repository;

    public BuscarMotivoCancelamentoUseCase(MotivoCancelamentoRepositoryPort repository) {
        this.repository = repository;
    }

    public MotivoCancelamento executar(Long id, String code) {
        return repository.buscarPorIdECode(id, code)
                .orElseThrow(() -> new MotivoCancelamentoInvalidoException("Motivo de cancelamento não encontrado: " + id));
    }
}
