package com.enterprise.gustadev.fintech_app.adapters.out.persistence.parserversao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParserVersaoJpaRepository extends JpaRepository<ParserVersaoEntity, Long> {
    List<ParserVersaoEntity> findByAtivoTrue();
    Optional<ParserVersaoEntity> findByBancoAndVersao(String banco, String versao);
}
