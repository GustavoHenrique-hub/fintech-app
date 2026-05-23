package com.enterprise.gustadev.fintech_app.domain.snapshotfinanceiro.port;

import com.enterprise.gustadev.fintech_app.domain.snapshotfinanceiro.model.SnapshotFinanceiro;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SnapshotFinanceiroRepositoryPort {
    SnapshotFinanceiro salvar(SnapshotFinanceiro snapshot);
    List<SnapshotFinanceiro> listarPorUsuario(UUID usuarioId);
    Optional<SnapshotFinanceiro> buscarPorUsuarioContaAnoMes(UUID usuarioId, UUID contaId, short ano, short mes);
    Optional<SnapshotFinanceiro> buscarPorId(UUID id);
}
