package com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacao;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransacaoJpaRepository extends JpaRepository<TransacaoEntity, Long> {

    @EntityGraph(attributePaths = {"conta"})
    List<TransacaoEntity> findByConta_UsuarioIdAndDeletedAtIsNullOrderByDataTransacaoDesc(Long usuarioId);

    @EntityGraph(attributePaths = {"conta"})
    List<TransacaoEntity> findByConta_IdAndDeletedAtIsNull(Long contaId);

    @EntityGraph(attributePaths = {"conta"})
    List<TransacaoEntity> findByExtratoIdAndDeletedAtIsNullOrderByDataTransacaoDesc(Long extratoId);

    @EntityGraph(attributePaths = {"conta"})
    Optional<TransacaoEntity> findByIdAndCode(Long id, String code);

    @Override
    @EntityGraph(attributePaths = {"conta"})
    Optional<TransacaoEntity> findById(Long id);

    @EntityGraph(attributePaths = {"conta"})
    Optional<TransacaoEntity> findByTransacaoEstornadaId(Long transacaoEstornadaId);

    @Query("""
        SELECT DISTINCT t FROM TransacaoEntity t
        LEFT JOIN FETCH t.categoria c
        JOIN FETCH t.conta co
        WHERE co.usuarioId = :usuarioId
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
      JOIN FETCH t.conta c
      WHERE t.id = :id
        AND t.code = :code
        AND c.id = :contaId
        AND c.code = :contaCode
        AND t.deletedAt IS NULL
  """)
    Optional<TransacaoEntity> buscarParaEstorno(
            @Param("id") Long id,
            @Param("code") String code,
            @Param("contaId") Long contaId,
            @Param("contaCode") String contaCode);

    /**
     * Direção efetiva replica {@link com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao#tipoEfetivo()}:
     * categoria AMBOS decide pelo sinal de {@code t.valor}. Estornos (indEstorno='S') e
     * transações já estornadas (estornadoAt preenchido) ficam de fora da soma.
     */
    @Query("""
        SELECT
          COALESCE(SUM(CASE
              WHEN c.tipo = :receita OR (c.tipo = :ambos AND t.valor > 0) THEN t.valor
              ELSE 0 END), 0),
          COALESCE(SUM(CASE
              WHEN c.tipo = :gasto THEN t.valor
              WHEN c.tipo = :ambos AND t.valor < 0 THEN -t.valor
              ELSE 0 END), 0)
        FROM TransacaoEntity t
        JOIN t.conta co
        JOIN t.categoria c
        WHERE co.usuarioId = :usuarioId
          AND co.usuarioCode = :usuarioCode
          AND co.id = :contaId
          AND co.code = :contaCode
          AND t.dataTransacao BETWEEN :inicio AND :fim
          AND t.deletedAt IS NULL
          AND t.estornadoAt IS NULL
          AND t.indEstorno = 'N'
    """)
    List<Object[]> somarPorUsuarioContaNoPeriodo(
            @Param("usuarioId") Long usuarioId,
            @Param("usuarioCode") String usuarioCode,
            @Param("contaId") Long contaId,
            @Param("contaCode") String contaCode,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim,
            @Param("receita") TipoCategoria receita,
            @Param("gasto") TipoCategoria gasto,
            @Param("ambos") TipoCategoria ambos);

}
