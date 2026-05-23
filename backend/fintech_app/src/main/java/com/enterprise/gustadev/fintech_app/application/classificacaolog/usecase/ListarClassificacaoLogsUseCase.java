package com.enterprise.gustadev.fintech_app.application.classificacaolog.usecase;

import com.enterprise.gustadev.fintech_app.domain.classificacaolog.model.ClassificacaoLog;
import com.enterprise.gustadev.fintech_app.domain.classificacaolog.port.ClassificacaoLogRepositoryPort;

import java.util.List;
import java.util.UUID;

public class ListarClassificacaoLogsUseCase {

    private final ClassificacaoLogRepositoryPort repository;

    public ListarClassificacaoLogsUseCase(ClassificacaoLogRepositoryPort repository) {
        this.repository = repository;
    }

    public List<ClassificacaoLog> executarPorTransacao(UUID transacaoId) {
        return repository.listarPorTransacao(transacaoId);
    }
}
