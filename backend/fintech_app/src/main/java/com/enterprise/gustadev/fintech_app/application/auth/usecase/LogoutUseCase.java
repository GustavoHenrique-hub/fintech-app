package com.enterprise.gustadev.fintech_app.application.auth.usecase;

import com.enterprise.gustadev.fintech_app.domain.auth.port.SessaoTokenRepositoryPort;

public class LogoutUseCase {

    private final SessaoTokenRepositoryPort sessaoRepository;

    public LogoutUseCase(SessaoTokenRepositoryPort sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    public void executar(String token) {
        sessaoRepository.deletarPorToken(token);
    }
}
