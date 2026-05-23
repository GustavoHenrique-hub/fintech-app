package com.enterprise.gustadev.fintech_app.application.consentimentolgpd.usecase;

import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.model.ConsentimentoLgpd;
import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.port.ConsentimentoLgpdRepositoryPort;

import java.util.List;
import java.util.UUID;

public class ListarConsentimentosLgpdUseCase {

    private final ConsentimentoLgpdRepositoryPort repository;

    public ListarConsentimentosLgpdUseCase(ConsentimentoLgpdRepositoryPort repository) {
        this.repository = repository;
    }

    public List<ConsentimentoLgpd> executar(UUID usuarioId) {
        return repository.listarPorUsuario(usuarioId);
    }
}
