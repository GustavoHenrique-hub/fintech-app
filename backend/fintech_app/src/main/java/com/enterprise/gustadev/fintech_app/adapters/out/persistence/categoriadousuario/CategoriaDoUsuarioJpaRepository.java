package com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoriadousuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaDoUsuarioJpaRepository extends JpaRepository<CategoriaDoUsuarioEntity, Long> {
    List<CategoriaDoUsuarioEntity> findByUsuarioId(Long usuarioId);
    Optional<CategoriaDoUsuarioEntity> findByUsuarioIdAndCategoriaId(Long usuarioId, Long categoriaId);
}
