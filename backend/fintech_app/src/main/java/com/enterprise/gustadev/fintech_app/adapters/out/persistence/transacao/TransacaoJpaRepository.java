package com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransacaoJpaRepository extends JpaRepository<TransacaoEntity, Long> {

    @EntityGraph(attributePaths = {"usuario", "conta"})
    List<TransacaoEntity> findByUsuario_IdUsuarioAndDeletedAtIsNullOrderByDataTransacaoDesc(Long usuarioId);

    @EntityGraph(attributePaths = {"usuario", "conta"})
    List<TransacaoEntity> findByConta_IdAndDeletedAtIsNull(Long contaId);

    @EntityGraph(attributePaths = {"usuario", "conta"})
    Optional<TransacaoEntity> findByIdAndCode(Long id, String code);

    @Override
    @EntityGraph(attributePaths = {"usuario", "conta"})
    Optional<TransacaoEntity> findById(Long id);

    @EntityGraph(attributePaths = {"usuario", "conta"})
    Optional<TransacaoEntity> findByTransacaoEstornadaId(Long transacaoEstornadaId);

    @Query("""
        SELECT DISTINCT t FROM TransacaoEntity t
        LEFT JOIN FETCH t.categoria c
        JOIN FETCH t.usuario u
        JOIN FETCH t.conta co
        WHERE u.idUsuario = :usuarioId
          AND t.dataTransacao BETWEEN :inicio AND :fim
          AND t.deletedAt IS NULL
        ORDER BY t.dataTransacao DESC
    """)
    List<TransacaoEntity> buscarPorUsuarioNoPeriodoComCategoriaEConta(
            @Param("usuarioId") Long usuarioId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim);

    @Query("""
      SELECT t FROM TransacaoEntity t
      JOIN FETCH t.usuario u
      JOIN FETCH t.conta c
      WHERE t.id = :id
        AND t.code = :code
        AND u.idUsuario = :usuarioId
        AND u.usuarioCode = :usuarioCode
        AND c.id = :contaId
        AND c.code = :contaCode
        AND t.deletedAt IS NULL
  """)
    Optional<TransacaoEntity> buscarParaEstorno(
            @Param("id") Long id,
            @Param("code") String code,
            @Param("usuarioId") Long usuarioId,
            @Param("usuarioCode") String usuarioCode,
            @Param("contaId") Long contaId,
            @Param("contaCode") String contaCode);

}
