package com.enterprise.gustadev.fintech_app.adapters.out.persistence.classificacaolog;

import com.enterprise.gustadev.fintech_app.domain.classificacaolog.model.ClassificacaoLog;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.EstrategiaClassificacao;
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
@Table(name = "classificacao_logs")
@Getter
@Setter
@NoArgsConstructor
public class ClassificacaoLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "transacao_id", nullable = false)
    private UUID transacaoId;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstrategiaClassificacao estrategia;

    @Column(name = "modelo_ia_versao", length = 50)
    private String modeloIaVersao;

    @Column(name = "prompt_versao", length = 20)
    private String promptVersao;

    @Column(name = "prompt_hash", length = 64)
    private String promptHash;

    @Column(name = "resposta_raw", columnDefinition = "TEXT")
    private String respostaRaw;

    @Column(name = "categoria_sugerida")
    private UUID categoriaSugerida;

    @Column(nullable = false)
    private short confianca;

    @Column(name = "tokens_usados")
    private Integer tokensUsados;

    @Column(name = "latencia_ms")
    private Integer latenciaMs;

    @Column(name = "categoria_final")
    private UUID categoriaFinal;

    @Column(nullable = false)
    private boolean corrigida = false;

    @Column(name = "corrigida_em")
    private OffsetDateTime corrigidaEm;

    @Column(name = "motivo_correcao", columnDefinition = "TEXT")
    private String motivoCorrecao;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    public static ClassificacaoLogEntity fromDomain(ClassificacaoLog domain) {
        ClassificacaoLogEntity entity = new ClassificacaoLogEntity();
        entity.id = domain.getId();
        entity.transacaoId = domain.getTransacaoId();
        entity.usuarioId = domain.getUsuarioId();
        entity.estrategia = domain.getEstrategia();
        entity.modeloIaVersao = domain.getModeloIaVersao();
        entity.promptVersao = domain.getPromptVersao();
        entity.promptHash = domain.getPromptHash();
        entity.respostaRaw = domain.getRespostaRaw();
        entity.categoriaSugerida = domain.getCategoriaSugerida();
        entity.confianca = domain.getConfianca();
        entity.tokensUsados = domain.getTokensUsados();
        entity.latenciaMs = domain.getLatenciaMs();
        entity.categoriaFinal = domain.getCategoriaFinal();
        entity.corrigida = domain.isCorrigida();
        entity.corrigidaEm = domain.getCorrigidaEm();
        entity.motivoCorrecao = domain.getMotivoCorrecao();
        entity.criadoEm = domain.getCriadoEm();
        return entity;
    }

    public ClassificacaoLog toDomain() {
        return new ClassificacaoLog(id, transacaoId, usuarioId, estrategia, modeloIaVersao,
                promptVersao, promptHash, respostaRaw, categoriaSugerida, confianca,
                tokensUsados, latenciaMs, categoriaFinal, corrigida, corrigidaEm, motivoCorrecao, criadoEm);
    }
}
