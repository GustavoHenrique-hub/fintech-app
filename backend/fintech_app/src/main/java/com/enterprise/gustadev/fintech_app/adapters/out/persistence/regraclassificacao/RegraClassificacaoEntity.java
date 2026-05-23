package com.enterprise.gustadev.fintech_app.adapters.out.persistence.regraclassificacao;

import com.enterprise.gustadev.fintech_app.domain.regraclassificacao.model.RegraClassificacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.CriadaPorRegraClassificacao;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "regras_classificacao")
@Getter
@Setter
@NoArgsConstructor
public class RegraClassificacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(nullable = false, length = 255)
    private String termo;

    @Column(name = "categoria_id", nullable = false)
    private UUID categoriaId;

    @Column(length = 100)
    private String subcategoria;

    @Column(name = "score_confianca", nullable = false, precision = 4, scale = 3)
    private BigDecimal scoreConfianca = BigDecimal.ONE;

    @Column(name = "vezes_aplicada", nullable = false)
    private int vezesAplicada = 0;

    @Column(name = "vezes_corretas", nullable = false)
    private int vezesCorretas = 0;

    @Column(name = "vezes_incorretas", nullable = false)
    private int vezesIncorretas = 0;

    @Column(name = "correcoes_consecutivas", nullable = false)
    private short correcoesConsecutivas = 0;

    @Column(nullable = false)
    private boolean congelada = false;

    @Column(name = "congelada_motivo", columnDefinition = "TEXT")
    private String congeladaMotivo;

    @Column(name = "congelada_ate")
    private LocalDate congeladaAte;

    @Enumerated(EnumType.STRING)
    @Column(name = "criada_por", nullable = false, length = 20)
    private CriadaPorRegraClassificacao criadaPor = CriadaPorRegraClassificacao.usuario;

    @Column(name = "ultima_aplicacao")
    private OffsetDateTime ultimaAplicacao;

    @Column(name = "ultima_correcao")
    private OffsetDateTime ultimaCorrecao;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    public static RegraClassificacaoEntity fromDomain(RegraClassificacao domain) {
        RegraClassificacaoEntity entity = new RegraClassificacaoEntity();
        entity.id = domain.getId();
        entity.usuarioId = domain.getUsuarioId();
        entity.termo = domain.getTermo();
        entity.categoriaId = domain.getCategoriaId();
        entity.subcategoria = domain.getSubcategoria();
        entity.scoreConfianca = domain.getScoreConfianca();
        entity.vezesAplicada = domain.getVezesAplicada();
        entity.vezesCorretas = domain.getVezesCorretas();
        entity.vezesIncorretas = domain.getVezesIncorretas();
        entity.correcoesConsecutivas = domain.getCorrecoesConsecutivas();
        entity.congelada = domain.isCongelada();
        entity.congeladaMotivo = domain.getCongeladaMotivo();
        entity.congeladaAte = domain.getCongeladaAte();
        entity.criadaPor = domain.getCriadaPor();
        entity.ultimaAplicacao = domain.getUltimaAplicacao();
        entity.ultimaCorrecao = domain.getUltimaCorrecao();
        entity.criadoEm = domain.getCriadoEm();
        return entity;
    }

    public RegraClassificacao toDomain() {
        return new RegraClassificacao(id, usuarioId, termo, categoriaId, subcategoria,
                scoreConfianca, vezesAplicada, vezesCorretas, vezesIncorretas, correcoesConsecutivas,
                congelada, congeladaMotivo, congeladaAte, criadaPor, ultimaAplicacao, ultimaCorrecao, criadoEm);
    }
}
