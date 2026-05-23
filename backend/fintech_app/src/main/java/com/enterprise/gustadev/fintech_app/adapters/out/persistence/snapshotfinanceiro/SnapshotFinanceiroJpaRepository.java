package com.enterprise.gustadev.fintech_app.adapters.out.persistence.snapshotfinanceiro;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SnapshotFinanceiroJpaRepository extends JpaRepository<SnapshotFinanceiroEntity, UUID> {
    List<SnapshotFinanceiroEntity> findByUsuarioIdOrderByAnoDescMesDesc(UUID usuarioId);
    Optional<SnapshotFinanceiroEntity> findByUsuarioIdAndContaIdAndAnoAndMes(UUID usuarioId, UUID contaId, short ano, short mes);
}
