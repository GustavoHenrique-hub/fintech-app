package com.enterprise.gustadev.fintech_app.adapters.out.persistence.processamentojob;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcessamentoJobJpaRepository extends JpaRepository<ProcessamentoJobEntity, Long> {
    List<ProcessamentoJobEntity> findByStatus(StatusJob status);
    List<ProcessamentoJobEntity> findByExtratoId(Long extratoId);
}
