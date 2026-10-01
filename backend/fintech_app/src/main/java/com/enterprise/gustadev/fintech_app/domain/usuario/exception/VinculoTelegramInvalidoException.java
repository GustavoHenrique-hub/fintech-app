package com.enterprise.gustadev.fintech_app.domain.usuario.exception;

/** Código de vínculo inexistente, expirado ou já usado. */
public class VinculoTelegramInvalidoException extends RuntimeException {
    public VinculoTelegramInvalidoException(String message) {
        super(message);
    }
}
