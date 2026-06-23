package com.enterprise.gustadev.fintech_app.domain.auth.port;

import com.enterprise.gustadev.fintech_app.domain.auth.model.SessaoToken;

import java.util.Optional;

public interface SessaoTokenRepositoryPort {
    SessaoToken salvar(SessaoToken sessao);
    Optional<SessaoToken> buscarPorToken(String token);
    void deletarPorToken(String token);
}
