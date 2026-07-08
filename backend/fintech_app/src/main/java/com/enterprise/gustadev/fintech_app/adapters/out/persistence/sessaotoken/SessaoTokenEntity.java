package com.enterprise.gustadev.fintech_app.adapters.out.persistence.sessaotoken;

import com.enterprise.gustadev.fintech_app.adapters.out.persistence.usuario.UsuarioEntity;
import com.enterprise.gustadev.fintech_app.domain.auth.model.SessaoToken;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    @Column(name = "sessao_token_id")
    private Long id;

    @Column(name = "sessao_token_code", unique = true, nullable = false, length = 6)
    private String code;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "usuario_id", nullable = false)
    private Long idUsuario;

    @Column(name = "usuario_code", nullable = false, length = 6)
    private String usuarioCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
            value = {
                    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id",
                            insertable = false, updatable = false),
                    @JoinColumn(name = "usuario_code", referencedColumnName = "usuario_code",
                            insertable = false, updatable = false)
            },
            foreignKey = @ForeignKey(name = "fk_sessao_tokens_usuario")
    )
    private UsuarioEntity usuario;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "expira_em", nullable = false)
    private LocalDateTime expiraEm;

    public static SessaoTokenEntity fromDomain(SessaoToken sessao) {
        SessaoTokenEntity entity = new SessaoTokenEntity();
        entity.id = sessao.getId();
        entity.code = sessao.getCode();
        entity.token = sessao.getToken();
        entity.idUsuario = sessao.getIdUsuario();
        entity.usuarioCode = sessao.getUsuarioCode();
        entity.criadoEm = sessao.getCriadoEm();
        entity.expiraEm = sessao.getExpiraEm();
        return entity;
    }

    public SessaoToken toDomain() {
        return new SessaoToken(id, code, token, idUsuario, usuarioCode, criadoEm, expiraEm);
    }
}
