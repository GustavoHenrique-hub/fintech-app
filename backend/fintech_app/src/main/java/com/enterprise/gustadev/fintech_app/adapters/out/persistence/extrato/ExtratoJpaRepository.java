package com.enterprise.gustadev.fintech_app.adapters.out.persistence.extrato;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExtratoJpaRepository extends JpaRepository<ExtratoEntity, UUID> {
    List<ExtratoEntity> findByUsuarioIdOrderByCriadoEmDesc(UUID usuarioId);
    Optional<ExtratoEntity> findByHashArquivo(String hashArquivo);
    Optional<ExtratoEntity> findByIdAndCode(UUID id, String code);
}
