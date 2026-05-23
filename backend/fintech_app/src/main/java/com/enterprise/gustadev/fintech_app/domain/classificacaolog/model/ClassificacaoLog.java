package com.enterprise.gustadev.fintech_app.domain.classificacaolog.model;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.EstrategiaClassificacao;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class ClassificacaoLog {

    private UUID id;
    private UUID transacaoId;
    private UUID usuarioId;
    private EstrategiaClassificacao estrategia;
    private String modeloIaVersao;
    private String promptVersao;
    private String promptHash;
    private String respostaRaw;
    private UUID categoriaSugerida;
    private short confianca;
    private Integer tokensUsados;
    private Integer latenciaMs;
    private UUID categoriaFinal;
    private boolean corrigida;
    private OffsetDateTime corrigidaEm;
    private String motivoCorrecao;
    private OffsetDateTime criadoEm;

    public ClassificacaoLog(UUID id, UUID transacaoId, UUID usuarioId, EstrategiaClassificacao estrategia,
                             String modeloIaVersao, String promptVersao, String promptHash,
                             String respostaRaw, UUID categoriaSugerida, short confianca,
                             Integer tokensUsados, Integer latenciaMs, UUID categoriaFinal,
                             boolean corrigida, OffsetDateTime corrigidaEm, String motivoCorrecao,
                             OffsetDateTime criadoEm) {
        this.id = id;
        this.transacaoId = transacaoId;
        this.usuarioId = usuarioId;
        this.estrategia = estrategia;
        this.modeloIaVersao = modeloIaVersao;
        this.promptVersao = promptVersao;
        this.promptHash = promptHash;
        this.respostaRaw = respostaRaw;
        this.categoriaSugerida = categoriaSugerida;
        this.confianca = confianca;
        this.tokensUsados = tokensUsados;
        this.latenciaMs = latenciaMs;
        this.categoriaFinal = categoriaFinal;
        this.corrigida = corrigida;
        this.corrigidaEm = corrigidaEm;
        this.motivoCorrecao = motivoCorrecao;
        this.criadoEm = criadoEm;
    }

    public ClassificacaoLog(UUID transacaoId, UUID usuarioId, EstrategiaClassificacao estrategia,
                             UUID categoriaSugerida, short confianca) {
        this(null, transacaoId, usuarioId, estrategia, null, null, null, null,
             categoriaSugerida, confianca, null, null, null, false, null, null, null);
    }
}
