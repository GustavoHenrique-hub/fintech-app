package com.enterprise.gustadev.fintech_app.domain.auth.port;

public interface SenhaEncoder {
    String encode(String senha);
    boolean matches(String senhaPlana, String senhaHash);
}
