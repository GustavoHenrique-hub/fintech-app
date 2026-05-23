package com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacaocancelada;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransacaoCanceladaJpaRepository extends JpaRepository<TransacaoCanceladaEntity, Long> {
    List<TransacaoCanceladaEntity> findByUsuarioId(Long usuarioId);
    Optional<TransacaoCanceladaEntity> findByTransacaoId(Long transacaoId);
}
