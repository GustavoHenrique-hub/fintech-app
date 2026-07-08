package com.enterprise.gustadev.fintech_app.adapters.out.persistence.notificacao;

import com.enterprise.gustadev.fintech_app.adapters.out.persistence.usuario.UsuarioEntity;
import com.enterprise.gustadev.fintech_app.domain.notificacao.model.Notificacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.CanalNotificacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.time.OffsetDateTime;

@Entity
@Table(name = "notificacoes")
@Getter
@Setter
@NoArgsConstructor
public class NotificacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificacao_id")
    private Long id;

    @Column(name = "notificacao_code", unique = true, nullable = false, length = 6)
    private String code;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

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
            foreignKey = @ForeignKey(name = "fk_notificacoes_usuario")
    )
    private UsuarioEntity usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CanalNotificacao canal;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(length = 255)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String mensagem;

    @Column(nullable = false)
    private boolean enviada = false;

    @Column(name = "enviada_em")
    private OffsetDateTime enviadaEm;

    @Column(columnDefinition = "TEXT")
    private String erro;

    @Column(nullable = false)
    private short tentativas = 0;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    public static NotificacaoEntity fromDomain(Notificacao domain) {
        NotificacaoEntity entity = new NotificacaoEntity();
        entity.id = domain.getId();
        entity.code = domain.getCode();
        entity.usuarioId = domain.getUsuarioId();
        entity.usuarioCode = domain.getUsuarioCode();
        entity.canal = domain.getCanal();
        entity.tipo = domain.getTipo();
        entity.titulo = domain.getTitulo();
        entity.mensagem = domain.getMensagem();
        entity.enviada = domain.isEnviada();
        entity.enviadaEm = domain.getEnviadaEm();
        entity.erro = domain.getErro();
        entity.tentativas = domain.getTentativas();
        entity.criadoEm = domain.getCriadoEm();
        return entity;
    }

    public Notificacao toDomain() {
        return new Notificacao(id, code, usuarioId, usuarioCode, canal, tipo, titulo, mensagem,
                enviada, enviadaEm, erro, tentativas, criadoEm);
    }
}
