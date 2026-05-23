package com.enterprise.gustadev.fintech_app.application.snapshotfinanceiro.usecase;

import com.enterprise.gustadev.fintech_app.domain.snapshotfinanceiro.model.SnapshotFinanceiro;
import com.enterprise.gustadev.fintech_app.domain.snapshotfinanceiro.port.SnapshotFinanceiroRepositoryPort;

import java.util.Optional;
import java.util.UUID;

public class BuscarSnapshotFinanceiroUseCase {

    private final SnapshotFinanceiroRepositoryPort repository;

    public BuscarSnapshotFinanceiroUseCase(SnapshotFinanceiroRepositoryPort repository) {
        this.repository = repository;
    }

    public Optional<SnapshotFinanceiro> executar(UUID usuarioId, UUID contaId, short ano, short mes) {
        return repository.buscarPorUsuarioContaAnoMes(usuarioId, contaId, ano, mes);
    }
}
