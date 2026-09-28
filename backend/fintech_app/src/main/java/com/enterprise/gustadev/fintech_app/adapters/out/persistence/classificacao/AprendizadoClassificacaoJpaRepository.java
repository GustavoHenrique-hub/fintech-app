package com.enterprise.gustadev.fintech_app.adapters.out.persistence.classificacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AprendizadoClassificacaoJpaRepository extends JpaRepository<AprendizadoClassificacaoEntity, Long> {
    Optional<AprendizadoClassificacaoEntity> findByUsuarioIdAndChave(Long usuarioId, String chave);
}
