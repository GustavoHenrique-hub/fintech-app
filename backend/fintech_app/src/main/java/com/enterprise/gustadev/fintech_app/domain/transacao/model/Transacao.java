package com.enterprise.gustadev.fintech_app.domain.transacao.model;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusRevisaoTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.util.CodeGenerator;
import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
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
    private Usuario usuario;
    private ContaFinanceira conta;
    private String indEstorno;
    private TipoTransacao tipo;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataTransacao;
    private Long categoriaId;
    private String estabelecimento;
    private OrigemTransacao origem;
    private StatusRevisaoTransacao statusRevisao;
    private Short confiancaIa;
    private boolean recorrente;
    private LocalDate periodoRecorrencia;
    private String observacao;
    private OffsetDateTime deletedAt;
    private int versao;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;
    /** Quando preenchido, esta linha é um estorno e aponta para o id da transação original revertida. */
    private Long transacaoEstornadaId;

    public Transacao(Long id, Usuario usuario, ContaFinanceira conta, String indEstorno,
                     TipoTransacao tipo, String descricao, BigDecimal valor,
                     LocalDate dataTransacao, Long categoriaId, String estabelecimento,
                     OrigemTransacao origem, StatusRevisaoTransacao statusRevisao,
                     Short confiancaIa, boolean recorrente, LocalDate periodoRecorrencia,
                     String observacao, OffsetDateTime deletedAt, int versao,
                     OffsetDateTime criadoEm, OffsetDateTime atualizadoEm) {
        this.id = id;
        this.usuario = usuario;
        this.conta = conta;
        this.indEstorno = indEstorno;
        this.tipo = tipo;
        this.descricao = descricao;
        this.valor = valor;
        this.dataTransacao = dataTransacao;
        this.categoriaId = categoriaId;
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

    public Transacao(Usuario usuario, ContaFinanceira conta, TipoTransacao tipo,
                     BigDecimal valor, LocalDate dataTransacao, Long categoriaId,
                     OrigemTransacao origem) {
        this(null, usuario, conta, "N", tipo, null,
             valor, dataTransacao, categoriaId, null, origem,
             StatusRevisaoTransacao.EXTRAIDA, null, false, null, null, null,
             1, OffsetDateTime.now(), null);
        this.code = CodeGenerator.gerar();
    }

    /**
     * Cria uma nova transação de estorno (cópia idêntica desta, com indEstorno='S'),
     * sem alterar a transação original. A nova linha recebe novo code e aponta para
     * o id da original via transacaoEstornadaId. A persistência insere uma nova linha.
     */
    public Transacao criarEstorno() {
        if ("S".equals(indEstorno)) {
            throw new TransacaoInvalidaException("Não é possível estornar uma transação que já é um estorno");
        }
        if (deletedAt != null) {
            throw new TransacaoInvalidaException("Transação deletada não pode ser estornada");
        }
        Transacao estorno = new Transacao(
                null, usuario, conta, "S", tipo, descricao, valor, dataTransacao,
                categoriaId, estabelecimento, origem, statusRevisao, confiancaIa,
                recorrente, periodoRecorrencia, observacao, null, 1,
                OffsetDateTime.now(), null);
        estorno.code = CodeGenerator.gerar();
        estorno.transacaoEstornadaId = this.id;
        return estorno;
    }

    public void validar() {
        if (usuario == null) {
            throw new TransacaoInvalidaException("Usuario é obrigatório");
        }
        if (conta == null) {
            throw new TransacaoInvalidaException("Conta é obrigatório");
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
        if (categoriaId == null) {
            throw new TransacaoInvalidaException("CategoriaId é obrigatório");
        }
    }
}
