package com.enterprise.gustadev.fintech_app.adapters.out.persistence.motivocancelamento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MotivoCancelamentoJpaRepository extends JpaRepository<MotivoCancelamentoEntity, Long> {
    List<MotivoCancelamentoEntity> findByAtivoTrue();
    Optional<MotivoCancelamentoEntity> findByIdAndCode(Long id, String code);
}
