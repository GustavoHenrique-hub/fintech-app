package com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoriadousuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoriaDoUsuarioJpaRepository extends JpaRepository<CategoriaDoUsuarioEntity, UUID> {
    List<CategoriaDoUsuarioEntity> findByUsuarioId(UUID usuarioId);
    Optional<CategoriaDoUsuarioEntity> findByUsuarioIdAndCategoriaId(UUID usuarioId, UUID categoriaId);
}
