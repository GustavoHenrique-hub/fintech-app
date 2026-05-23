package com.enterprise.gustadev.fintech_app.domain.parserversao.model;

import com.enterprise.gustadev.fintech_app.domain.parserversao.exception.ParserVersaoNaoEncontradaException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class ParserVersao {

    private Long id;
    private String banco;
    private String versao;
    private boolean ativo;
    private BigDecimal scoreQualidade;
    private int totalUsos;
    private int totalErros;
    private String descricao;
    private OffsetDateTime depreciadoEm;

    public ParserVersao(Long id, String banco, String versao, boolean ativo,
                        BigDecimal scoreQualidade, int totalUsos, int totalErros,
                        String descricao, OffsetDateTime depreciadoEm) {
        this.id = id;
        this.banco = banco;
        this.versao = versao;
        this.ativo = ativo;
        this.scoreQualidade = scoreQualidade;
        this.totalUsos = totalUsos;
        this.totalErros = totalErros;
        this.descricao = descricao;
        this.depreciadoEm = depreciadoEm;
    }

    public ParserVersao(String banco, String versao, boolean ativo, BigDecimal scoreQualidade, String descricao) {
        this(null, banco, versao, ativo, scoreQualidade, 0, 0, descricao, null);
    }

    public void validar() {
        if (banco == null || banco.isBlank()) {
            throw new ParserVersaoNaoEncontradaException("Banco é obrigatório");
        }
        if (versao == null || versao.isBlank()) {
            throw new ParserVersaoNaoEncontradaException("Versão é obrigatória");
        }
    }
}
