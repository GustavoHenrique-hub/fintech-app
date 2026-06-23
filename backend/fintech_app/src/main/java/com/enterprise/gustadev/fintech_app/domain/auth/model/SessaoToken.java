package com.enterprise.gustadev.fintech_app.domain.auth.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SessaoToken {

    private Long id;
    private String token;
    private Long idUsuario;
    private String usuarioCode;
    private LocalDateTime criadoEm;
    private LocalDateTime expiraEm;

    public SessaoToken(String token, Long idUsuario, String usuarioCode,
                       LocalDateTime criadoEm, LocalDateTime expiraEm) {
        this.token = token;
        this.idUsuario = idUsuario;
        this.usuarioCode = usuarioCode;
        this.criadoEm = criadoEm;
        this.expiraEm = expiraEm;
    }

    public SessaoToken(Long id, String token, Long idUsuario, String usuarioCode,
                       LocalDateTime criadoEm, LocalDateTime expiraEm) {
        this.id = id;
        this.token = token;
        this.idUsuario = idUsuario;
        this.usuarioCode = usuarioCode;
        this.criadoEm = criadoEm;
        this.expiraEm = expiraEm;
    }

    public boolean expirou() {
        return LocalDateTime.now().isAfter(expiraEm);
    }
}
