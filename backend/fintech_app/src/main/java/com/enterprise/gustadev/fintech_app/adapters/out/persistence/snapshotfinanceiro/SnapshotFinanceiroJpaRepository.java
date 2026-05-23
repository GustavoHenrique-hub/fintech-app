package com.enterprise.gustadev.fintech_app.adapters.out.persistence.snapshotfinanceiro;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SnapshotFinanceiroJpaRepository extends JpaRepository<SnapshotFinanceiroEntity, Long> {
    List<SnapshotFinanceiroEntity> findByUsuarioIdOrderByAnoDescMesDesc(Long usuarioId);
    Optional<SnapshotFinanceiroEntity> findByUsuarioIdAndContaIdAndAnoAndMes(Long usuarioId, Long contaId, short ano, short mes);
}
