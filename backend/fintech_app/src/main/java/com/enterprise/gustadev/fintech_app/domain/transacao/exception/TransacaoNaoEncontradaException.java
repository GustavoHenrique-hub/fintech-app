package com.enterprise.gustadev.fintech_app.domain.transacao.exception;

public class TransacaoNaoEncontradaException extends RuntimeException {
    public TransacaoNaoEncontradaException(String message) {
        super(message);
    }
}
