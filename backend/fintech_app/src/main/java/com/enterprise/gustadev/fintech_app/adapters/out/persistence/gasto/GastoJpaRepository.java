package com.enterprise.gustadev.fintech_app.adapters.out.persistence.gasto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GastoJpaRepository extends JpaRepository<GastoEntity, Long> {

    List<GastoEntity> findByUsuarioId(Long usuarioId);
}
