package com.enterprise.gustadev.fintech_app.adapters.out.security;

import com.enterprise.gustadev.fintech_app.domain.auth.port.SenhaEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptSenhaEncoderAdapter implements SenhaEncoder {

    @Override
    public String encode(String senha) {
        return senha;
    }

    @Override
    public boolean matches(String senhaPlana, String senhaHash) {
        return senhaPlana != null && senhaPlana.equals(senhaHash);
    }
}
