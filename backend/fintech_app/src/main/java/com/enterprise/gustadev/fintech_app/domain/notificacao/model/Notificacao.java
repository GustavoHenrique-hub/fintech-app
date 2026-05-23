package com.enterprise.gustadev.fintech_app.domain.notificacao.model;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.CanalNotificacao;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class Notificacao {

    private UUID id;
    private UUID usuarioId;
    private CanalNotificacao canal;
    private String tipo;
    private String titulo;
    private String mensagem;
    private boolean enviada;
    private OffsetDateTime enviadaEm;
    private String erro;
    private short tentativas;
    private OffsetDateTime criadoEm;

    public Notificacao(UUID id, UUID usuarioId, CanalNotificacao canal, String tipo,
                        String titulo, String mensagem, boolean enviada, OffsetDateTime enviadaEm,
                        String erro, short tentativas, OffsetDateTime criadoEm) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.canal = canal;
        this.tipo = tipo;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.enviada = enviada;
        this.enviadaEm = enviadaEm;
        this.erro = erro;
        this.tentativas = tentativas;
        this.criadoEm = criadoEm;
    }

    public Notificacao(UUID usuarioId, CanalNotificacao canal, String tipo,
                        String titulo, String mensagem) {
        this(null, usuarioId, canal, tipo, titulo, mensagem, false, null, null, (short) 0, null);
    }
}
