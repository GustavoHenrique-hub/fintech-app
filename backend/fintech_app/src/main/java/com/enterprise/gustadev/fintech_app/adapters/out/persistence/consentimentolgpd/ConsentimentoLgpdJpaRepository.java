package com.enterprise.gustadev.fintech_app.adapters.out.persistence.consentimentolgpd;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConsentimentoLgpdJpaRepository extends JpaRepository<ConsentimentoLgpdEntity, UUID> {
    List<ConsentimentoLgpdEntity> findByUsuarioId(UUID usuarioId);
}
