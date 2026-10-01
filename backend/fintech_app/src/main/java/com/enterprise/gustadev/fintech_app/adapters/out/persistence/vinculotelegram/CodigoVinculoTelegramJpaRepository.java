package com.enterprise.gustadev.fintech_app.adapters.out.persistence.vinculotelegram;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodigoVinculoTelegramJpaRepository extends JpaRepository<CodigoVinculoTelegramEntity, Long> {
    Optional<CodigoVinculoTelegramEntity> findByCodigo(String codigo);
}
