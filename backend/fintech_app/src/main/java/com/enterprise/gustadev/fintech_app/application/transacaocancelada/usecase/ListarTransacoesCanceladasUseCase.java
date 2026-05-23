package com.enterprise.gustadev.fintech_app.application.transacaocancelada.usecase;

import com.enterprise.gustadev.fintech_app.domain.transacaocancelada.model.TransacaoCancelada;
import com.enterprise.gustadev.fintech_app.domain.transacaocancelada.port.TransacaoCanceladaRepositoryPort;

import java.util.List;

public class ListarTransacoesCanceladasUseCase {

    private final TransacaoCanceladaRepositoryPort repository;

    public ListarTransacoesCanceladasUseCase(TransacaoCanceladaRepositoryPort repository) {
        this.repository = repository;
    }

    public List<TransacaoCancelada> executar(Long usuarioId) {
        return repository.listarPorUsuario(usuarioId);
    }
}
