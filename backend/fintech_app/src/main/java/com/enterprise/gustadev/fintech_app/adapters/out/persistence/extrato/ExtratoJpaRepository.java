package com.enterprise.gustadev.fintech_app.adapters.out.persistence.extrato;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExtratoJpaRepository extends JpaRepository<ExtratoEntity, Long> {

    @Query("SELECT e FROM ExtratoEntity e WHERE e.usuarioId = :usuarioId AND e.indDelete = 'N' ORDER BY e.criadoEm DESC")
    List<ExtratoEntity> findByUsuarioIdOrderByCriadoEmDesc(@Param("usuarioId") Long usuarioId);

    @Query("SELECT e FROM ExtratoEntity e WHERE e.hashArquivo = :hashArquivo AND e.indDelete = 'N'")
    Optional<ExtratoEntity> findByHashArquivo(@Param("hashArquivo") String hashArquivo);

    @Query("SELECT e FROM ExtratoEntity e WHERE e.id = :id AND e.code = :code AND e.indDelete = 'N'")
    Optional<ExtratoEntity> findByIdAndCode(@Param("id") Long id, @Param("code") String code);
}
