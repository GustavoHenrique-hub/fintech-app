package com.enterprise.gustadev.fintech_app.application.transacaocancelada.usecase;

import com.enterprise.gustadev.fintech_app.domain.transacaocancelada.model.TransacaoCancelada;
import com.enterprise.gustadev.fintech_app.domain.transacaocancelada.port.TransacaoCanceladaRepositoryPort;

public class CancelarTransacaoUseCase {

    private final TransacaoCanceladaRepositoryPort repository;

    public CancelarTransacaoUseCase(TransacaoCanceladaRepositoryPort repository) {
        this.repository = repository;
    }

    public TransacaoCancelada executar(TransacaoCancelada transacaoCancelada) {
        transacaoCancelada.validar();
        return repository.salvar(transacaoCancelada);
    }
}
