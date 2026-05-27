package com.enterprise.gustadev.fintech_app.domain.banco.exception;

public class BancoInvalidoException extends RuntimeException {
    public BancoInvalidoException(String message) {
        super(message);
    }
}
