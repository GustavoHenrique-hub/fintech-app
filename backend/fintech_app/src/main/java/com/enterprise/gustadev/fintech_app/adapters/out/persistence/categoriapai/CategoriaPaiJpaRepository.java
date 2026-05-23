package com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoriapai;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoriaPaiJpaRepository extends JpaRepository<CategoriaPaiEntity, UUID> {
    List<CategoriaPaiEntity> findByPaiId(UUID paiId);
    Optional<CategoriaPaiEntity> findByCategoriaId(UUID categoriaId);
    void deleteByCategoriaId(UUID categoriaId);
}
