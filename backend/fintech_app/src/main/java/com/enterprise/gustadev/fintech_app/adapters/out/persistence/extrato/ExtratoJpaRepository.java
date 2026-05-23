package com.enterprise.gustadev.fintech_app.adapters.out.persistence.extrato;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExtratoJpaRepository extends JpaRepository<ExtratoEntity, Long> {
    List<ExtratoEntity> findByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);
    Optional<ExtratoEntity> findByHashArquivo(String hashArquivo);
    Optional<ExtratoEntity> findByIdAndCode(Long id, String code);
}
