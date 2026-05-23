package com.enterprise.gustadev.fintech_app.adapters.out.persistence.contafinanceira;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContaFinanceiraJpaRepository extends JpaRepository<ContaFinanceiraEntity, UUID> {
    List<ContaFinanceiraEntity> findByUsuarioId(UUID usuarioId);
    Optional<ContaFinanceiraEntity> findByIdAndCode(UUID id, String code);
}
