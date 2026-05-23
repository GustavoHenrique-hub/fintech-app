package com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoria;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaJpaRepository extends JpaRepository<CategoriaEntity, Long> {
    List<CategoriaEntity> findByPadraoTrue();
    List<CategoriaEntity> findByTipo(TipoCategoria tipo);
    Optional<CategoriaEntity> findByIdAndCode(Long id, String code);
}
