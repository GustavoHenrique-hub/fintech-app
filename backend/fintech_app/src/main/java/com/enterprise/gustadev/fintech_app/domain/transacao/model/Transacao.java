package com.enterprise.gustadev.fintech_app.domain.transacao.model;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusRevisaoTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoInvalidaException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class Transacao {

    private UUID id;
    private String code;
    private UUID usuarioId;
    private UUID contaId;
    private UUID extratoId;
    private UUID transacaoEstornoId;
    private TipoTransacao tipo;
    private String descricaoOriginal;
    private String descricaoUsuario;
    private String descricaoNormalizada;
    private BigDecimal valor;
    private LocalDate dataTransacao;
    private OffsetDateTime dataLancamento;
    private UUID categoriaId;
    private String subcategoria;
    private String estabelecimento;
    private OrigemTransacao origem;
    private StatusRevisaoTransacao statusRevisao;
    private Short confiancaIa;
    private boolean recorrente;
    private String periodoRecorrencia;
    private String observacao;
    private OffsetDateTime deletedAt;
    private int versao;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;

    public Transacao(UUID id, UUID usuarioId, UUID contaId, UUID extratoId,
                     UUID transacaoEstornoId, TipoTransacao tipo, String descricaoOriginal,
                     String descricaoUsuario, String descricaoNormalizada, BigDecimal valor,
                     LocalDate dataTransacao, OffsetDateTime dataLancamento, UUID categoriaId,
                     String subcategoria, String estabelecimento, OrigemTransacao origem,
                     StatusRevisaoTransacao statusRevisao, Short confiancaIa, boolean recorrente,
                     String periodoRecorrencia, String observacao, OffsetDateTime deletedAt,
                     int versao, OffsetDateTime criadoEm, OffsetDateTime atualizadoEm) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.contaId = contaId;
        this.extratoId = extratoId;
        this.transacaoEstornoId = transacaoEstornoId;
        this.tipo = tipo;
        this.descricaoOriginal = descricaoOriginal;
        this.descricaoUsuario = descricaoUsuario;
        this.descricaoNormalizada = descricaoNormalizada;
        this.valor = valor;
        this.dataTransacao = dataTransacao;
        this.dataLancamento = dataLancamento;
        this.categoriaId = categoriaId;
        this.subcategoria = subcategoria;
        this.estabelecimento = estabelecimento;
        this.origem = origem;
        this.statusRevisao = statusRevisao;
        this.confiancaIa = confiancaIa;
        this.recorrente = recorrente;
        this.periodoRecorrencia = periodoRecorrencia;
        this.observacao = observacao;
        this.deletedAt = deletedAt;
        this.versao = versao;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public Transacao(UUID usuarioId, UUID contaId, TipoTransacao tipo,
                     BigDecimal valor, LocalDate dataTransacao, OrigemTransacao origem) {
        this(null, usuarioId, contaId, null, null, tipo, null, null, null,
             valor, dataTransacao, null, null, null, null, origem,
             StatusRevisaoTransacao.extraida, null, false, null, null, null,
             1, null, null);
    }

    public void validar() {
        if (usuarioId == null) {
            throw new TransacaoInvalidaException("UsuarioId é obrigatório");
        }
        if (contaId == null) {
            throw new TransacaoInvalidaException("ContaId é obrigatório");
        }
        if (tipo == null) {
            throw new TransacaoInvalidaException("Tipo é obrigatório");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransacaoInvalidaException("Valor deve ser maior que zero");
        }
        if (dataTransacao == null) {
            throw new TransacaoInvalidaException("Data da transação é obrigatória");
        }
        if (origem == null) {
            throw new TransacaoInvalidaException("Origem é obrigatória");
        }
    }
}
