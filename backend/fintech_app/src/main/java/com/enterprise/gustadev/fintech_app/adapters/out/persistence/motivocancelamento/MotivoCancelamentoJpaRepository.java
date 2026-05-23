package com.enterprise.gustadev.fintech_app.adapters.out.persistence.motivocancelamento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MotivoCancelamentoJpaRepository extends JpaRepository<MotivoCancelamentoEntity, UUID> {
    List<MotivoCancelamentoEntity> findByAtivoTrue();
    Optional<MotivoCancelamentoEntity> findByIdAndCode(UUID id, String code);
}
