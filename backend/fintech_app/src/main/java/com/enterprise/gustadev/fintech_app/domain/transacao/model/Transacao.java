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
    /**
     * {@code true} quando o valor desta transação já está refletido no saldo da conta.
     * Lançamentos importados nascem com {@code false} e só entram no saldo ao serem
     * confirmados; {@code null} vem de linhas antigas, gravadas quando a importação
     * ainda aplicava o saldo na hora — e por isso contam como aplicadas.
     */
    private Boolean saldoAplicado;

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
        this.saldoAplicado = Boolean.TRUE;
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
        estorno.saldoAplicado = this.saldoAplicado;
        return estorno;
    }

    /** {@code true} quando o valor já está no saldo da conta (linhas antigas, sem a flag, contam como aplicadas). */
    public boolean saldoJaAplicado() {
        return saldoAplicado == null || saldoAplicado;
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

    /**
     * Confirma a revisão manual de um lançamento importado de extrato: só é permitida
     * a partir de PENDENTE_REVISAO — o usuário já viu o lançamento e a conta a que
     * pertence antes de clicar em "Revisado".
     */
    public void confirmarRevisao() {
        if (statusRevisao != StatusRevisaoTransacao.PENDENTE_REVISAO) {
            throw new TransacaoInvalidaException(
                    "Só é possível confirmar revisão de uma transação PENDENTE_REVISAO (status atual: " + statusRevisao + ")");
        }
        this.statusRevisao = StatusRevisaoTransacao.CONFIRMADA;
        this.atualizadoEm = OffsetDateTime.now();
    }

    /**
     * Desfaz ("estorna") uma revisão já confirmada: o lançamento volta para
     * PENDENTE_REVISAO para ser classificado de novo. Não vale para estornos
     * financeiros nem para transações já estornadas — essas não têm mais revisão.
     */
    public void desfazerRevisao() {
        if (statusRevisao != StatusRevisaoTransacao.CONFIRMADA) {
            throw new TransacaoInvalidaException(
                    "Só é possível estornar a revisão de uma transação CONFIRMADA (status atual: " + statusRevisao + ")");
        }
        if ("S".equals(indEstorno) || estornadoAt != null) {
            throw new TransacaoInvalidaException("Transação estornada não pode ter a revisão desfeita");
        }
        this.statusRevisao = StatusRevisaoTransacao.PENDENTE_REVISAO;
        this.atualizadoEm = OffsetDateTime.now();
    }

    /** Soma {@code delta} (positivo ou negativo) à confiança da IA, mantendo-a entre 0 e 100. */
    public void ajustarConfianca(int delta) {
        int base = confiancaIa != null ? confiancaIa : 0;
        this.confiancaIa = (short) Math.max(0, Math.min(100, base + delta));
    }

    /**
     * Tira o lançamento da contabilidade de receitas/gastos sem apagá-lo: é o que
     * acontece quando o usuário revisa um lançamento importado e diz que aquilo é
     * movimentação de economias — o valor passa a viver no ledger de
     * {@code MovimentacaoEconomia} e esta linha some das listagens
     * (elas filtram {@code deletedAt IS NULL}).
     */
    public void ignorar() {
        if (statusRevisao != StatusRevisaoTransacao.PENDENTE_REVISAO) {
            throw new TransacaoInvalidaException(
                    "Só é possível ignorar uma transação PENDENTE_REVISAO (status atual: " + statusRevisao + ")");
        }
        this.statusRevisao = StatusRevisaoTransacao.IGNORADA;
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
