package com.enterprise.gustadev.fintech_app.application.snapshotfinanceiro.usecase;

import com.enterprise.gustadev.fintech_app.domain.snapshotfinanceiro.model.SnapshotFinanceiro;
import com.enterprise.gustadev.fintech_app.domain.snapshotfinanceiro.port.SnapshotFinanceiroRepositoryPort;

import java.util.List;

public class ListarSnapshotsFinanceirosUseCase {

    private final SnapshotFinanceiroRepositoryPort repository;

    public ListarSnapshotsFinanceirosUseCase(SnapshotFinanceiroRepositoryPort repository) {
        this.repository = repository;
    }

    public List<SnapshotFinanceiro> executar(Long usuarioId) {
        return repository.listarPorUsuario(usuarioId);
    }
}
