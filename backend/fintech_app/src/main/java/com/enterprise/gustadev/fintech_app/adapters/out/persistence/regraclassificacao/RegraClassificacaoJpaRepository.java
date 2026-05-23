package com.enterprise.gustadev.fintech_app.adapters.out.persistence.regraclassificacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RegraClassificacaoJpaRepository extends JpaRepository<RegraClassificacaoEntity, UUID> {
    List<RegraClassificacaoEntity> findByUsuarioId(UUID usuarioId);
}
