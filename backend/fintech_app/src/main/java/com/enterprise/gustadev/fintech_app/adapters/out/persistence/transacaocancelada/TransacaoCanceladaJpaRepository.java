package com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacaocancelada;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransacaoCanceladaJpaRepository extends JpaRepository<TransacaoCanceladaEntity, UUID> {
    List<TransacaoCanceladaEntity> findByUsuarioId(UUID usuarioId);
    Optional<TransacaoCanceladaEntity> findByTransacaoId(UUID transacaoId);
}
