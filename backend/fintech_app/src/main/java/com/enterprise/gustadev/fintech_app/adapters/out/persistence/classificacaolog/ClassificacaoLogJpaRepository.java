package com.enterprise.gustadev.fintech_app.adapters.out.persistence.classificacaolog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClassificacaoLogJpaRepository extends JpaRepository<ClassificacaoLogEntity, UUID> {
    List<ClassificacaoLogEntity> findByTransacaoId(UUID transacaoId);
    List<ClassificacaoLogEntity> findByUsuarioId(UUID usuarioId);
}
