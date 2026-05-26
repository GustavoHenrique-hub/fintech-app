package com.enterprise.gustadev.fintech_app.adapters.out.persistence.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUsuarioRepository
        extends JpaRepository<UsuarioEntity, Long> {
}
