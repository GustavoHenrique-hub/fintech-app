package com.enterprise.gustadev.fintech_app.adapters.in.web.auth.dto;

import com.enterprise.gustadev.fintech_app.domain.auth.model.SessaoToken;

import java.time.LocalDateTime;

public record LoginResponseDTO(
        String token,
        Long idUsuario,
        String usuarioCode,
        LocalDateTime expiraEm
) {
    public static LoginResponseDTO fromDomain(SessaoToken sessao) {
        return new LoginResponseDTO(
                sessao.getToken(),
                sessao.getIdUsuario(),
                sessao.getUsuarioCode(),
                sessao.getExpiraEm()
        );
    }
}
