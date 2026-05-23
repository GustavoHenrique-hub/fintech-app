package com.enterprise.gustadev.fintech_app.adapters.out.persistence.notificacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoJpaRepository extends JpaRepository<NotificacaoEntity, Long> {
    List<NotificacaoEntity> findByUsuarioId(Long usuarioId);
    List<NotificacaoEntity> findByEnviadaFalse();
}
