package com.enterprise.gustadev.fintech_app.adapters.out.persistence.parserversao;

import com.enterprise.gustadev.fintech_app.domain.parserversao.model.ParserVersao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "parser_versoes")
@Getter
@Setter
@NoArgsConstructor
public class ParserVersaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parser_versao_id")
    private Long id;

    @Column(name = "parser_versao_code", unique = true, nullable = false, length = 6)
    private String code;

    @Column(nullable = false, length = 100)
    private String banco;

    @Column(nullable = false, length = 20)
    private String versao;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "score_qualidade", precision = 4, scale = 3)
    private BigDecimal scoreQualidade;

    @Column(name = "total_usos", nullable = false)
    private int totalUsos = 0;

    @Column(name = "total_erros", nullable = false)
    private int totalErros = 0;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "depreciado_em")
    private OffsetDateTime depreciadoEm;

    public static ParserVersaoEntity fromDomain(ParserVersao domain) {
        ParserVersaoEntity entity = new ParserVersaoEntity();
        entity.id = domain.getId();
        entity.code = domain.getCode();
        entity.banco = domain.getBanco();
        entity.versao = domain.getVersao();
        entity.ativo = domain.isAtivo();
        entity.scoreQualidade = domain.getScoreQualidade();
        entity.totalUsos = domain.getTotalUsos();
        entity.totalErros = domain.getTotalErros();
        entity.descricao = domain.getDescricao();
        entity.depreciadoEm = domain.getDepreciadoEm();
        return entity;
    }

    public ParserVersao toDomain() {
        return new ParserVersao(id, code, banco, versao, ativo, scoreQualidade, totalUsos, totalErros, descricao, depreciadoEm);
    }
}
