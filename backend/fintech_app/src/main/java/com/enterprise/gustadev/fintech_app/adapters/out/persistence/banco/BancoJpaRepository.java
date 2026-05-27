package com.enterprise.gustadev.fintech_app.adapters.out.persistence.banco;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BancoJpaRepository extends JpaRepository<BancoEntity, Long> {
    Optional<BancoEntity> findByIdAndCode(Long id, String code);
}
