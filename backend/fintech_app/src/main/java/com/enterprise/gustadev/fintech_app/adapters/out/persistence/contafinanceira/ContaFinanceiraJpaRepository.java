package com.enterprise.gustadev.fintech_app.adapters.out.persistence.contafinanceira;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContaFinanceiraJpaRepository extends JpaRepository<ContaFinanceiraEntity, Long> {
    List<ContaFinanceiraEntity> findByUsuarioId(Long usuarioId);
    Optional<ContaFinanceiraEntity> findByIdAndCode(Long id, String code);
}
