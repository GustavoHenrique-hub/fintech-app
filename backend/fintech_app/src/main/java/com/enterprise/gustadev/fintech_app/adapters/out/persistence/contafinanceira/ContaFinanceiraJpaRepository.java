package com.enterprise.gustadev.fintech_app.adapters.out.persistence.contafinanceira;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContaFinanceiraJpaRepository extends JpaRepository<ContaFinanceiraEntity, UUID> {
    List<ContaFinanceiraEntity> findByUsuarioId(UUID usuarioId);
}
