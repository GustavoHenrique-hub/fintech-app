package com.enterprise.gustadev.fintech_app.application.consentimentolgpd.usecase;

import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.model.ConsentimentoLgpd;
import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.port.ConsentimentoLgpdRepositoryPort;

public class RegistrarConsentimentoLgpdUseCase {

    private final ConsentimentoLgpdRepositoryPort repository;

    public RegistrarConsentimentoLgpdUseCase(ConsentimentoLgpdRepositoryPort repository) {
        this.repository = repository;
    }

    public ConsentimentoLgpd executar(ConsentimentoLgpd consentimento) {
        consentimento.validar();
        return repository.salvar(consentimento);
    }
}
