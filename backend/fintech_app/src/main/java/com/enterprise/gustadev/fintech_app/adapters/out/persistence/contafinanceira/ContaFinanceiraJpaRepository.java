package com.enterprise.gustadev.fintech_app.adapters.out.persistence.contafinanceira;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContaFinanceiraJpaRepository extends JpaRepository<ContaFinanceiraEntity, Long> {

    @Query("SELECT c FROM ContaFinanceiraEntity c WHERE c.usuarioId = :usuarioId AND c.indDelete = 'N'")
    List<ContaFinanceiraEntity> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT c FROM ContaFinanceiraEntity c WHERE c.id = :id AND c.code = :code AND c.indDelete = 'N'")
    Optional<ContaFinanceiraEntity> findByIdAndCode(@Param("id") Long id, @Param("code") String code);
}
