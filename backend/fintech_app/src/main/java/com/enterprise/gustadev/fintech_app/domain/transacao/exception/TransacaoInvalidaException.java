package com.enterprise.gustadev.fintech_app.domain.transacao.exception;

public class TransacaoInvalidaException extends RuntimeException {
    public TransacaoInvalidaException(String message) {
        super(message);
    }
}
