package com.enterprise.gustadev.fintech_app.domain.snapshotfinanceiro.port;

import com.enterprise.gustadev.fintech_app.domain.snapshotfinanceiro.model.SnapshotFinanceiro;

import java.util.List;
import java.util.Optional;

public interface SnapshotFinanceiroRepositoryPort {
    SnapshotFinanceiro salvar(SnapshotFinanceiro snapshot);
    List<SnapshotFinanceiro> listarPorUsuario(Long usuarioId);
    Optional<SnapshotFinanceiro> buscarPorUsuarioContaAnoMes(Long usuarioId, Long contaId, short ano, short mes);
    Optional<SnapshotFinanceiro> buscarPorId(Long id);
}
