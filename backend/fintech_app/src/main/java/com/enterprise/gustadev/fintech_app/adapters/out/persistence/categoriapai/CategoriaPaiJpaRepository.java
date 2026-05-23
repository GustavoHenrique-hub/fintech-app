package com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoriapai;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaPaiJpaRepository extends JpaRepository<CategoriaPaiEntity, Long> {
    List<CategoriaPaiEntity> findByPaiId(Long paiId);
    Optional<CategoriaPaiEntity> findByCategoriaId(Long categoriaId);
    void deleteByCategoriaId(Long categoriaId);
}
