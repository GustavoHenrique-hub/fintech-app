package com.enterprise.gustadev.fintech_app.application.classificacaolog.usecase;

import com.enterprise.gustadev.fintech_app.domain.classificacaolog.model.ClassificacaoLog;
import com.enterprise.gustadev.fintech_app.domain.classificacaolog.port.ClassificacaoLogRepositoryPort;

public class RegistrarClassificacaoLogUseCase {

    private final ClassificacaoLogRepositoryPort repository;

    public RegistrarClassificacaoLogUseCase(ClassificacaoLogRepositoryPort repository) {
        this.repository = repository;
    }

    public ClassificacaoLog executar(ClassificacaoLog log) {
        return repository.salvar(log);
    }
}
