package com.enterprise.gustadev.fintech_app.application.usuario.usecase;

import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.port.ConsentimentoLgpdRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.usuario.exception.UsuarioInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.CodigoVinculoTelegram;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.CodigoVinculoTelegramRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;

/** Gera o código de uso único que o usuário leva ao bot via {@code /start <codigo>}. */
public class GerarCodigoVinculoTelegramUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final CodigoVinculoTelegramRepositoryPort codigoRepository;
    private final ConsentimentoLgpdRepositoryPort consentimentoRepository;

    public GerarCodigoVinculoTelegramUseCase(UsuarioRepositoryPort usuarioRepository,
                                             CodigoVinculoTelegramRepositoryPort codigoRepository,
                                             ConsentimentoLgpdRepositoryPort consentimentoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.codigoRepository = codigoRepository;
        this.consentimentoRepository = consentimentoRepository;
    }

    public CodigoVinculoTelegram executar(Long usuarioId) {
        usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new UsuarioInvalidoException("Usuario nao encontrado"));
        if (!ConsentimentoBotTelegram.ativo(consentimentoRepository, usuarioId)) {
            throw new UsuarioInvalidoException(
                    "Aceite o consentimento de comunicação pelo Telegram antes de vincular");
        }
        return codigoRepository.salvar(CodigoVinculoTelegram.novo(usuarioId));
    }
}
