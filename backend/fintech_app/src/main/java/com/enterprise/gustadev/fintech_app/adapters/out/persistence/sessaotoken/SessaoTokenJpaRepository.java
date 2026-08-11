package com.enterprise.gustadev.fintech_app.adapters.out.persistence.sessaotoken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessaoTokenJpaRepository extends JpaRepository<SessaoTokenEntity, Long> {
    Optional<SessaoTokenEntity> findByToken(String token);
    void deleteByToken(String token);
}
