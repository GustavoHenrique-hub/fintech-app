package com.enterprise.gustadev.fintech_app.adapters.out.persistence.consentimentolgpd;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsentimentoLgpdJpaRepository extends JpaRepository<ConsentimentoLgpdEntity, Long> {
    List<ConsentimentoLgpdEntity> findByUsuarioId(Long usuarioId);
}
