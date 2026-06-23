package com.enterprise.gustadev.fintech_app.adapters.out.persistence.sessaotoken;

import com.enterprise.gustadev.fintech_app.domain.auth.model.SessaoToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessao_tokens")
@Getter
@Setter
@NoArgsConstructor
public class SessaoTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "usuario_code", nullable = false)
    private String usuarioCode;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "expira_em", nullable = false)
    private LocalDateTime expiraEm;

    public static SessaoTokenEntity fromDomain(SessaoToken sessao) {
        SessaoTokenEntity entity = new SessaoTokenEntity();
        entity.id = sessao.getId();
        entity.token = sessao.getToken();
        entity.idUsuario = sessao.getIdUsuario();
        entity.usuarioCode = sessao.getUsuarioCode();
        entity.criadoEm = sessao.getCriadoEm();
        entity.expiraEm = sessao.getExpiraEm();
        return entity;
    }

    public SessaoToken toDomain() {
        return new SessaoToken(id, token, idUsuario, usuarioCode, criadoEm, expiraEm);
    }
}
