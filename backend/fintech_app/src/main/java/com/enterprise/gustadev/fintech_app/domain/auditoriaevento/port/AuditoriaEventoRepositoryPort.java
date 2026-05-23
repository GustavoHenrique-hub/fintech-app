package com.enterprise.gustadev.fintech_app.domain.auditoriaevento.port;

import com.enterprise.gustadev.fintech_app.domain.auditoriaevento.model.AuditoriaEvento;

import java.util.List;

public interface AuditoriaEventoRepositoryPort {
    AuditoriaEvento salvar(AuditoriaEvento evento);
    List<AuditoriaEvento> listarPorUsuario(Long usuarioId);
    List<AuditoriaEvento> listarPorCorrelation(Long correlationId);
}
