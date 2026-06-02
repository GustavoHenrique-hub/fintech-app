package com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacaocancelada;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransacaoCanceladaJpaRepository extends JpaRepository<TransacaoCanceladaEntity, Long> {

    @Query("SELECT tc FROM TransacaoCanceladaEntity tc JOIN tc.transacao t WHERE t.usuario.idUsuario = :usuarioId")
    List<TransacaoCanceladaEntity> findByTransacaoUsuarioId(@Param("usuarioId") Long usuarioId);

    Optional<TransacaoCanceladaEntity> findByTransacaoId(Long transacaoId);
}
