package com.enterprise.gustadev.fintech_app.adapters.out.persistence.notificacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificacaoJpaRepository extends JpaRepository<NotificacaoEntity, UUID> {
    List<NotificacaoEntity> findByUsuarioId(UUID usuarioId);
    List<NotificacaoEntity> findByEnviadaFalse();
}
