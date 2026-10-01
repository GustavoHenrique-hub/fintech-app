package com.enterprise.gustadev.fintech_app.domain.usuario.exception;

/** Nenhum usuário com consentimento ativo está ligado ao chat informado. */
public class UsuarioNaoVinculadoException extends RuntimeException {
    public UsuarioNaoVinculadoException(String message) {
        super(message);
    }
}
