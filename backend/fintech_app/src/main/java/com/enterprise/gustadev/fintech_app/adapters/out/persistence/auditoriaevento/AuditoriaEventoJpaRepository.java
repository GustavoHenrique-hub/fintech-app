package com.enterprise.gustadev.fintech_app.adapters.out.persistence.auditoriaevento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditoriaEventoJpaRepository extends JpaRepository<AuditoriaEventoEntity, UUID> {
    List<AuditoriaEventoEntity> findByUsuarioIdOrderByCriadoEmDesc(UUID usuarioId);
    List<AuditoriaEventoEntity> findByCorrelationId(UUID correlationId);
}
