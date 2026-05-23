package com.enterprise.gustadev.fintech_app.application.extrato.usecase;

import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;


public class BuscarExtratoUseCase {

    private final ExtratoRepositoryPort repository;

    public BuscarExtratoUseCase(ExtratoRepositoryPort repository) {
        this.repository = repository;
    }

    public Extrato executar(Long id, String code) {
        return repository.buscarPorIdECode(id, code)
                .orElseThrow(() -> new ExtratoInvalidoException("Extrato não encontrado: " + id));
    }
}
