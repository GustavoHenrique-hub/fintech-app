package com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoria;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoriaJpaRepository extends JpaRepository<CategoriaEntity, UUID> {
    List<CategoriaEntity> findByPadraoTrue();
    List<CategoriaEntity> findByTipo(TipoCategoria tipo);
}
