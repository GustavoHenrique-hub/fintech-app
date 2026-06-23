package com.enterprise.gustadev.fintech_app.adapters.out.security;

import com.enterprise.gustadev.fintech_app.domain.auth.port.SenhaEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptSenhaEncoderAdapter implements SenhaEncoder {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String senha) {
        return encoder.encode(senha);
    }

    @Override
    public boolean matches(String senhaPlana, String senhaHash) {
        return encoder.matches(senhaPlana, senhaHash);
    }
}
