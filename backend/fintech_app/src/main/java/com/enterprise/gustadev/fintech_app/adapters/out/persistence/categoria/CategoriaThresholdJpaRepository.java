package com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoriaThresholdJpaRepository extends JpaRepository<CategoriaThresholdEntity, UUID> {
    Optional<CategoriaThresholdEntity> findByCategoriaId(UUID categoriaId);
}
