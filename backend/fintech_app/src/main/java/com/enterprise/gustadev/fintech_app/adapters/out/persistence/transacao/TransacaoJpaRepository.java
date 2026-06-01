package com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransacaoJpaRepository extends JpaRepository<TransacaoEntity, Long> {
    List<TransacaoEntity> findByUsuarioIdAndDeletedAtIsNullOrderByDataTransacaoDesc(Long usuarioId);
    List<TransacaoEntity> findByContaIdAndDeletedAtIsNull(Long contaId);
    Optional<TransacaoEntity> findByIdAndCode(Long id, String code);

    @Query("""
        SELECT DISTINCT t FROM TransacaoEntity t
        LEFT JOIN FETCH t.categoria c
        JOIN FETCH t.conta co
        WHERE t.usuarioId = :usuarioId
          AND t.dataTransacao BETWEEN :inicio AND :fim
          AND t.deletedAt IS NULL
        ORDER BY t.dataTransacao DESC
    """)
    List<TransacaoEntity> buscarPorUsuarioNoPeriodoComCategoriaEConta(
            @Param("usuarioId") Long usuarioId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim);

    @Query("" ,nativeQuery = true)
}


