package com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.port;

import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.model.ConsentimentoLgpd;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsentimentoLgpdRepositoryPort {
    ConsentimentoLgpd salvar(ConsentimentoLgpd consentimento);
    List<ConsentimentoLgpd> listarPorUsuario(UUID usuarioId);
    Optional<ConsentimentoLgpd> buscarPorId(UUID id);
}
