package com.enterprise.gustadev.fintech_app.adapters.out.persistence.notificacao;

import com.enterprise.gustadev.fintech_app.domain.notificacao.model.Notificacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.CanalNotificacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "notificacoes")
@Getter
@Setter
@NoArgsConstructor
public class NotificacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

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
        entity.usuarioId = domain.getUsuarioId();
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
        return new Notificacao(id, usuarioId, canal, tipo, titulo, mensagem,
                enviada, enviadaEm, erro, tentativas, criadoEm);
    }
}
