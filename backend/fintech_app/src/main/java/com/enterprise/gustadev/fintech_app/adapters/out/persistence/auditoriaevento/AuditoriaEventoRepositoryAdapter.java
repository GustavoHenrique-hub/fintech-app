package com.enterprise.gustadev.fintech_app.adapters.out.persistence.auditoriaevento;

import com.enterprise.gustadev.fintech_app.domain.auditoriaevento.model.AuditoriaEvento;
import com.enterprise.gustadev.fintech_app.domain.auditoriaevento.port.AuditoriaEventoRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuditoriaEventoRepositoryAdapter implements AuditoriaEventoRepositoryPort {

    private final AuditoriaEventoJpaRepository jpaRepository;

    public AuditoriaEventoRepositoryAdapter(AuditoriaEventoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public AuditoriaEvento salvar(AuditoriaEvento evento) {
        return jpaRepository.save(AuditoriaEventoEntity.fromDomain(evento)).toDomain();
    }

    @Override
    public List<AuditoriaEvento> listarPorUsuario(Long usuarioId) {
        return jpaRepository.findByUsuarioIdOrderByCriadoEmDesc(usuarioId)
                .stream().map(AuditoriaEventoEntity::toDomain).toList();
    }

    @Override
    public List<AuditoriaEvento> listarPorCorrelation(Long correlationId) {
        return jpaRepository.findByCorrelationId(correlationId)
                .stream().map(AuditoriaEventoEntity::toDomain).toList();
    }
}
