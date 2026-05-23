package com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaThresholdJpaRepository extends JpaRepository<CategoriaThresholdEntity, Long> {
    Optional<CategoriaThresholdEntity> findByCategoriaId(Long categoriaId);
}
