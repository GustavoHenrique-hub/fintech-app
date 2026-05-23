package com.enterprise.gustadev.fintech_app.domain.regraclassificacao.model;

import com.enterprise.gustadev.fintech_app.domain.regraclassificacao.exception.RegraClassificacaoInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.CriadaPorRegraClassificacao;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class RegraClassificacao {

    private UUID id;
    private UUID usuarioId;
    private String termo;
    private UUID categoriaId;
    private String subcategoria;
    private BigDecimal scoreConfianca;
    private int vezesAplicada;
    private int vezesCorretas;
    private int vezesIncorretas;
    private short correcoesConsecutivas;
    private boolean congelada;
    private String congeladaMotivo;
    private LocalDate congeladaAte;
    private CriadaPorRegraClassificacao criadaPor;
    private OffsetDateTime ultimaAplicacao;
    private OffsetDateTime ultimaCorrecao;
    private OffsetDateTime criadoEm;

    public RegraClassificacao(UUID id, UUID usuarioId, String termo, UUID categoriaId,
                               String subcategoria, BigDecimal scoreConfianca, int vezesAplicada,
                               int vezesCorretas, int vezesIncorretas, short correcoesConsecutivas,
                               boolean congelada, String congeladaMotivo, LocalDate congeladaAte,
                               CriadaPorRegraClassificacao criadaPor, OffsetDateTime ultimaAplicacao,
                               OffsetDateTime ultimaCorrecao, OffsetDateTime criadoEm) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.termo = termo;
        this.categoriaId = categoriaId;
        this.subcategoria = subcategoria;
        this.scoreConfianca = scoreConfianca;
        this.vezesAplicada = vezesAplicada;
        this.vezesCorretas = vezesCorretas;
        this.vezesIncorretas = vezesIncorretas;
        this.correcoesConsecutivas = correcoesConsecutivas;
        this.congelada = congelada;
        this.congeladaMotivo = congeladaMotivo;
        this.congeladaAte = congeladaAte;
        this.criadaPor = criadaPor;
        this.ultimaAplicacao = ultimaAplicacao;
        this.ultimaCorrecao = ultimaCorrecao;
        this.criadoEm = criadoEm;
    }

    public RegraClassificacao(UUID usuarioId, String termo, UUID categoriaId,
                               String subcategoria, CriadaPorRegraClassificacao criadaPor) {
        this(null, usuarioId, termo, categoriaId, subcategoria, BigDecimal.ONE,
             0, 0, 0, (short) 0, false, null, null, criadaPor, null, null, null);
    }

    public void validar() {
        if (usuarioId == null) {
            throw new RegraClassificacaoInvalidaException("UsuarioId é obrigatório");
        }
        if (termo == null || termo.isBlank()) {
            throw new RegraClassificacaoInvalidaException("Termo é obrigatório");
        }
        if (categoriaId == null) {
            throw new RegraClassificacaoInvalidaException("CategoriaId é obrigatório");
        }
    }
}
