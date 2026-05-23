package com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransacaoJpaRepository extends JpaRepository<TransacaoEntity, UUID> {
    List<TransacaoEntity> findByUsuarioIdAndDeletedAtIsNullOrderByDataTransacaoDesc(UUID usuarioId);
    List<TransacaoEntity> findByContaIdAndDeletedAtIsNull(UUID contaId);
    List<TransacaoEntity> findByExtratoIdAndDeletedAtIsNull(UUID extratoId);
    Optional<TransacaoEntity> findByIdAndCode(UUID id, String code);
}
