package com.enterprise.gustadev.fintech_app.adapters.out.persistence.parserversao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParserVersaoJpaRepository extends JpaRepository<ParserVersaoEntity, UUID> {
    List<ParserVersaoEntity> findByAtivoTrue();
    Optional<ParserVersaoEntity> findByBancoAndVersao(String banco, String versao);
}
