package com.enterprise.gustadev.fintech_app.domain.transacao.model;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusRevisaoTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.util.CodeGenerator;
import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoInvalidaException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
public class Transacao {

    private Long id;
    private String code;
    private ContaFinanceira conta;
    private String indEstorno;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataTransacao;
    private Long categoriaId;
    private String categoriaCode;
    /** Tipo da categoria associada (RECEITA/GASTO/AMBOS), resolvido pelo chamador — não persistido em transacoes. */
    private TipoCategoria categoriaTipo;
    private String estabelecimento;
    private OrigemTransacao origem;
    private StatusRevisaoTransacao statusRevisao;
    private Short confiancaIa;
    private boolean recorrente;
    private LocalDate periodoRecorrencia;
    private String observacao;
    private OffsetDateTime estornadoAt;
    private int versao;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;
    /** Quando preenchido, esta linha é um estorno e aponta para o id da transação original revertida. */
    private Long transacaoEstornadaId;
    private OffsetDateTime deletedAt;
    /** Quando preenchida (origem=importado), aponta para o extrato que gerou esta transação. */
    private Long extratoId;
    private String extratoCode;

    public Transacao(Long id, ContaFinanceira conta, String indEstorno,
                     String descricao, BigDecimal valor,
                     LocalDate dataTransacao, Long categoriaId, String categoriaCode,
                     String estabelecimento,
                     OrigemTransacao origem, StatusRevisaoTransacao statusRevisao,
                     Short confiancaIa, boolean recorrente, LocalDate periodoRecorrencia,
                     String observacao, int versao,
                     OffsetDateTime criadoEm, OffsetDateTime atualizadoEm, OffsetDateTime estornadoAt) {
        this.id = id;
        this.conta = conta;
        this.indEstorno = indEstorno;
        this.descricao = descricao;
        this.valor = valor;
        this.dataTransacao = dataTransacao;
        this.categoriaId = categoriaId;
        this.categoriaCode = categoriaCode;
        this.estabelecimento = estabelecimento;
        this.origem = origem;
        this.statusRevisao = statusRevisao;
        this.confiancaIa = confiancaIa;
        this.recorrente = recorrente;
        this.periodoRecorrencia = periodoRecorrencia;
        this.observacao = observacao;
        this.estornadoAt = estornadoAt;
        this.versao = versao;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public Transacao(ContaFinanceira conta,
                     BigDecimal valor, LocalDate dataTransacao, Long categoriaId,
                     String categoriaCode, OrigemTransacao origem) {
        this(null, conta, "N", null,
             valor, dataTransacao, categoriaId, categoriaCode, null, origem,
             StatusRevisaoTransacao.EXTRAIDA, null, false, null, null,
             1, OffsetDateTime.now(), null, null);
        this.code = CodeGenerator.gerar();
    }

    /**
     * Cria uma nova transação de estorno (cópia idêntica desta, com indEstorno='S').
     * Marca esta transação como estornada (estornadoAt) e cria nova linha com indEstorno='S'.
     */
    public Transacao criarEstorno() {
        if ("S".equals(indEstorno)) {
            throw new TransacaoInvalidaException("Não é possível estornar uma transação que já é um estorno");
        }
        this.estornadoAt = OffsetDateTime.now();
        Transacao estorno = new Transacao(
                null, conta, "S", descricao, valor, dataTransacao,
                categoriaId, categoriaCode, estabelecimento, origem, statusRevisao, confiancaIa,
                recorrente, periodoRecorrencia, observacao, 1,
                OffsetDateTime.now(), null, null);
        estorno.code = CodeGenerator.gerar();
        estorno.transacaoEstornadaId = this.id;
        estorno.categoriaTipo = this.categoriaTipo;
        return estorno;
    }

    /**
     * Direção efetiva (RECEITA/GASTO) desta transação. Para categoria RECEITA/GASTO
     * a direção vem direto da categoria; para AMBOS (ex: categoria "Outros"), o
     * sinal de {@code valor} desempata (negativo = GASTO, positivo = RECEITA).
     */
    public TipoTransacao tipoEfetivo() {
        if (categoriaTipo == null) {
            throw new TransacaoInvalidaException("Tipo da categoria da transação não foi resolvido");
        }
        return switch (categoriaTipo) {
            case RECEITA -> TipoTransacao.RECEITA;
            case GASTO -> TipoTransacao.GASTO;
            case AMBOS -> valor.signum() < 0 ? TipoTransacao.GASTO : TipoTransacao.RECEITA;
        };
    }

    public void arquivar() {
        this.statusRevisao = StatusRevisaoTransacao.ARQUIVADA;
        this.deletedAt = OffsetDateTime.now();
        this.atualizadoEm = OffsetDateTime.now();
    }

    public void validar() {
        if (conta == null) {
            throw new TransacaoInvalidaException("Conta é obrigatório");
        }
        if (categoriaId == null) {
            throw new TransacaoInvalidaException("CategoriaId é obrigatório");
        }
        if (categoriaTipo == null) {
            throw new TransacaoInvalidaException("Tipo da categoria é obrigatório");
        }
        if (valor == null || valor.signum() == 0) {
            throw new TransacaoInvalidaException("Valor deve ser diferente de zero");
        }
        if (categoriaTipo != TipoCategoria.AMBOS && valor.signum() < 0) {
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
