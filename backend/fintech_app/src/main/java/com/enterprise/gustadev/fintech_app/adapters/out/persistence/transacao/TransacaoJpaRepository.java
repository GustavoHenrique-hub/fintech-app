package com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransacaoJpaRepository extends JpaRepository<TransacaoEntity, Long> {
    List<TransacaoEntity> findByUsuarioIdAndDeletedAtIsNullOrderByDataTransacaoDesc(Long usuarioId);
    List<TransacaoEntity> findByContaIdAndDeletedAtIsNull(Long contaId);
    List<TransacaoEntity> findByExtratoIdAndDeletedAtIsNull(Long extratoId);
    Optional<TransacaoEntity> findByIdAndCode(Long id, String code);
}
