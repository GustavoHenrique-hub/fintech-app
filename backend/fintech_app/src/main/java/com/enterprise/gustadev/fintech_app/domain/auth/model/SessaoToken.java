package com.enterprise.gustadev.fintech_app.domain.auth.model;

import com.enterprise.gustadev.fintech_app.domain.shared.util.CodeGenerator;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SessaoToken {

    private Long id;
    private String code;
    private String token;
    private Long idUsuario;
    private String usuarioCode;
    private LocalDateTime criadoEm;
    private LocalDateTime expiraEm;

    /**
     * Construtor de criação de nova sessão (gera novo {@code code}).
     */
    public SessaoToken(String token, Long idUsuario, String usuarioCode,
                       LocalDateTime criadoEm, LocalDateTime expiraEm) {
        this.code = CodeGenerator.gerar();
        this.token = token;
        this.idUsuario = idUsuario;
        this.usuarioCode = usuarioCode;
        this.criadoEm = criadoEm;
        this.expiraEm = expiraEm;
    }

    /**
     * Construtor de reconstrução (a partir da persistência): recebe {@code code} existente.
     */
    public SessaoToken(Long id, String code, String token, Long idUsuario, String usuarioCode,
                       LocalDateTime criadoEm, LocalDateTime expiraEm) {
        this.id = id;
        this.code = code;
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
