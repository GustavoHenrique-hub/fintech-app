package com.enterprise.gustadev.fintech_app.adapters.out.persistence.processamentojob;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProcessamentoJobJpaRepository extends JpaRepository<ProcessamentoJobEntity, UUID> {
    List<ProcessamentoJobEntity> findByStatus(StatusJob status);
    List<ProcessamentoJobEntity> findByExtratoId(UUID extratoId);
}
