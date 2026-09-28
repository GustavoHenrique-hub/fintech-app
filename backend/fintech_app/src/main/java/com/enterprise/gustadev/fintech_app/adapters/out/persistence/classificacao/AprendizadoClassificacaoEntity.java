package com.enterprise.gustadev.fintech_app.adapters.out.persistence.classificacao;

import com.enterprise.gustadev.fintech_app.domain.classificacao.model.AprendizadoClassificacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "aprendizado_classificacao",
        uniqueConstraints = @UniqueConstraint(name = "uk_aprendizado_classificacao_usuario_chave",
                columnNames = {"usuario_id", "chave"}))
@Getter
@Setter
@NoArgsConstructor
public class AprendizadoClassificacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aprendizado_classificacao_id")
    private Long id;

    @Column(name = "aprendizado_classificacao_code", unique = true, nullable = false, length = 6)
    private String code;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false, length = 120)
    private String chave;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoTransacao tipo;

    @Column(name = "categoria_id")
    private Long categoriaId;

    @Column(name = "categoria_code", length = 6)
    private String categoriaCode;

    @Column(nullable = false)
    private int acertos;

    @Column(nullable = false)
    private int erros;

    @Column(name = "atualizado_em", nullable = false)
    private OffsetDateTime atualizadoEm;

    public static AprendizadoClassificacaoEntity fromDomain(AprendizadoClassificacao domain) {
        AprendizadoClassificacaoEntity entity = new AprendizadoClassificacaoEntity();
        entity.id = domain.getId();
        entity.code = domain.getCode();
        entity.usuarioId = domain.getUsuarioId();
        entity.chave = domain.getChave();
        entity.tipo = domain.getTipo();
        entity.categoriaId = domain.getCategoriaId();
        entity.categoriaCode = domain.getCategoriaCode();
        entity.acertos = domain.getAcertos();
        entity.erros = domain.getErros();
        entity.atualizadoEm = domain.getAtualizadoEm();
        return entity;
    }

    public AprendizadoClassificacao toDomain() {
        return new AprendizadoClassificacao(id, code, usuarioId, chave, tipo, categoriaId, categoriaCode,
                acertos, erros, atualizadoEm);
    }
}
