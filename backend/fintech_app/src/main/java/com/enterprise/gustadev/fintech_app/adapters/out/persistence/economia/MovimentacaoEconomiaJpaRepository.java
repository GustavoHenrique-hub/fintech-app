package com.enterprise.gustadev.fintech_app.adapters.out.persistence.economia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovimentacaoEconomiaJpaRepository extends JpaRepository<MovimentacaoEconomiaEntity, Long> {

    @Query("SELECT m FROM MovimentacaoEconomiaEntity m WHERE m.contaId = :contaId ORDER BY m.dataMovimentacao DESC")
    List<MovimentacaoEconomiaEntity> findByContaIdOrderByDataMovimentacaoDesc(@Param("contaId") Long contaId);
}
