package com.enterprise.gustadev.fintech_app.adapters.out.persistence.auditoriaevento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriaEventoJpaRepository extends JpaRepository<AuditoriaEventoEntity, Long> {
    List<AuditoriaEventoEntity> findByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);
    List<AuditoriaEventoEntity> findByCorrelationId(Long correlationId);
}
