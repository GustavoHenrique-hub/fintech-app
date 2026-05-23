package com.enterprise.gustadev.fintech_app.application.extrato.usecase;

import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;

public class CriarExtratoUseCase {

    private final ExtratoRepositoryPort repository;

    public CriarExtratoUseCase(ExtratoRepositoryPort repository) {
        this.repository = repository;
    }

    public Extrato executar(Extrato extrato) {
        extrato.validar();
        repository.buscarPorHash(extrato.getHashArquivo()).ifPresent(e -> {
            throw new ExtratoInvalidoException("Extrato duplicado: hash já processado");
        });
        return repository.salvar(extrato);
    }
}
