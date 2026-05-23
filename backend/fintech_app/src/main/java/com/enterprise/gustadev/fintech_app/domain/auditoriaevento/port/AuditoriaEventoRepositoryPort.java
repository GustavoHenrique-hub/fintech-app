package com.enterprise.gustadev.fintech_app.domain.auditoriaevento.port;

import com.enterprise.gustadev.fintech_app.domain.auditoriaevento.model.AuditoriaEvento;

import java.util.List;
import java.util.UUID;

public interface AuditoriaEventoRepositoryPort {
    AuditoriaEvento salvar(AuditoriaEvento evento);
    List<AuditoriaEvento> listarPorUsuario(UUID usuarioId);
    List<AuditoriaEvento> listarPorCorrelation(UUID correlationId);
}
