package com.enterprise.gustadev.fintech_app.domain.notificacao.model;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.CanalNotificacao;
import com.enterprise.gustadev.fintech_app.domain.shared.util.CodeGenerator;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class Notificacao {

    private Long id;
    private String code;
    private Long usuarioId;
    private String usuarioCode;
    private CanalNotificacao canal;
    private String tipo;
    private String titulo;
    private String mensagem;
    private boolean enviada;
    private OffsetDateTime enviadaEm;
    private String erro;
    private short tentativas;
    private OffsetDateTime criadoEm;

    public Notificacao(Long id, String code, Long usuarioId, String usuarioCode,
                        CanalNotificacao canal, String tipo,
                        String titulo, String mensagem, boolean enviada, OffsetDateTime enviadaEm,
                        String erro, short tentativas, OffsetDateTime criadoEm) {
        this.id = id;
        this.code = code;
        this.usuarioId = usuarioId;
        this.usuarioCode = usuarioCode;
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

    public Notificacao(Long usuarioId, String usuarioCode, CanalNotificacao canal, String tipo,
                        String titulo, String mensagem) {
        this(null, CodeGenerator.gerar(), usuarioId, usuarioCode, canal, tipo, titulo, mensagem,
             false, null, null, (short) 0, OffsetDateTime.now());
    }
}
