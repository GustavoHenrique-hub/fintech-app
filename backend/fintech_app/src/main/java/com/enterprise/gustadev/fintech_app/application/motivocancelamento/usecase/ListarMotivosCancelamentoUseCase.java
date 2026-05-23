package com.enterprise.gustadev.fintech_app.application.motivocancelamento.usecase;

import com.enterprise.gustadev.fintech_app.domain.motivocancelamento.model.MotivoCancelamento;
import com.enterprise.gustadev.fintech_app.domain.motivocancelamento.port.MotivoCancelamentoRepositoryPort;

import java.util.List;

public class ListarMotivosCancelamentoUseCase {

    private final MotivoCancelamentoRepositoryPort repository;

    public ListarMotivosCancelamentoUseCase(MotivoCancelamentoRepositoryPort repository) {
        this.repository = repository;
    }

    public List<MotivoCancelamento> executar() {
        return repository.listarAtivos();
    }
}
