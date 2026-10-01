package com.enterprise.gustadev.fintech_app.adapters.out.persistence.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByEmail(String email);

    @Query("select u from UsuarioEntity u where u.telegramChatID = :chatId")
    Optional<UsuarioEntity> buscarPorTelegramChatId(@Param("chatId") Long chatId);
}
