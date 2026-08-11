package com.enterprise.gustadev.fintech_app.domain.contafinanceira.model;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.util.CodeGenerator;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class ContaFinanceira {

    private Long id;
    private String code;
    private Long usuarioId;
    private String usuarioCode;
    private TipoConta tipo;
    private Long bancoId;
    private String bancoCode;
    private BigDecimal saldoInicial;
    private BigDecimal saldoAtual;
    private BigDecimal saldoEconomias;
    private boolean padrao;
    private boolean ativa;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;
    private String indDelete;
    private OffsetDateTime deletedAt;

    public ContaFinanceira(Long id, Long usuarioId, String usuarioCode, TipoConta tipo,
                           Long bancoId, String bancoCode, BigDecimal saldoInicial, BigDecimal saldoAtual,
                           BigDecimal saldoEconomias,
                           boolean padrao, boolean ativa, OffsetDateTime criadoEm, OffsetDateTime atualizadoEm,
                           String indDelete, OffsetDateTime deletedAt) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioCode = usuarioCode;
        this.tipo = tipo;
        this.bancoId = bancoId;
        this.bancoCode = bancoCode;
        this.saldoInicial = saldoInicial;
        this.saldoAtual = saldoAtual;
        this.saldoEconomias = saldoEconomias != null ? saldoEconomias : BigDecimal.ZERO;
        this.padrao = padrao;
        this.ativa = ativa;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.indDelete = indDelete != null ? indDelete : "N";
        this.deletedAt = deletedAt;
    }

    /** Construtor de compatibilidade — mantém as chamadas existentes que ainda não conhecem o campo saldoEconomias. */
    public ContaFinanceira(Long id, Long usuarioId, String usuarioCode, TipoConta tipo,
                           Long bancoId, String bancoCode, BigDecimal saldoInicial, BigDecimal saldoAtual,
                           boolean padrao, boolean ativa, OffsetDateTime criadoEm, OffsetDateTime atualizadoEm,
                           String indDelete, OffsetDateTime deletedAt) {
        this(id, usuarioId, usuarioCode, tipo, bancoId, bancoCode, saldoInicial, saldoAtual,
                BigDecimal.ZERO, padrao, ativa, criadoEm, atualizadoEm, indDelete, deletedAt);
    }

    public ContaFinanceira(Long usuarioId, String usuarioCode, TipoConta tipo,
                           Long bancoId, String bancoCode, BigDecimal saldoInicial, boolean padrao) {
        this(null, usuarioId, usuarioCode, tipo, bancoId, bancoCode, saldoInicial,
                saldoInicial != null ? saldoInicial : BigDecimal.ZERO,
                BigDecimal.ZERO,
                padrao, true, OffsetDateTime.now(), null, "N", null);
        this.code = CodeGenerator.gerar();
    }

    public void inicializarSaldo() {
        this.saldoAtual = saldoInicial != null ? saldoInicial : BigDecimal.ZERO;
    }

    public void aplicarTransacao(TipoTransacao tipo, BigDecimal valor) {
        BigDecimal base = saldoAtual != null ? saldoAtual : BigDecimal.ZERO;
        this.saldoAtual = tipo == TipoTransacao.RECEITA ? base.add(valor) : base.subtract(valor);
    }

    public void reverterTransacao(TipoTransacao tipo, BigDecimal valor) {
        BigDecimal base = saldoAtual != null ? saldoAtual : BigDecimal.ZERO;
        this.saldoAtual = tipo == TipoTransacao.RECEITA ? base.subtract(valor) : base.add(valor);
    }

    /**
     * Move parte do saldo disponível ({@code saldoAtual}) para o sub-saldo de economias da MESMA conta.
     * Não gera receita nem gasto — é uma reserva interna.
     */
    public void aportarEconomia(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ContaFinanceiraInvalidaException("Valor do aporte deve ser positivo");
        }
        BigDecimal disponivel = saldoAtual != null ? saldoAtual : BigDecimal.ZERO;
        if (disponivel.compareTo(valor) < 0) {
            throw new ContaFinanceiraInvalidaException(
                    "Saldo disponível insuficiente para aportar em economias");
        }
        BigDecimal economias = saldoEconomias != null ? saldoEconomias : BigDecimal.ZERO;
        this.saldoAtual = disponivel.subtract(valor);
        this.saldoEconomias = economias.add(valor);
    }

    /**
     * Devolve parte do sub-saldo de economias para o saldo disponível ({@code saldoAtual}) da MESMA conta.
     * Não gera receita nem gasto — é o inverso de {@link #aportarEconomia(BigDecimal)}.
     */
    public void resgatarEconomia(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ContaFinanceiraInvalidaException("Valor do resgate deve ser positivo");
        }
        BigDecimal economias = saldoEconomias != null ? saldoEconomias : BigDecimal.ZERO;
        if (economias.compareTo(valor) < 0) {
            throw new ContaFinanceiraInvalidaException(
                    "Saldo em economias insuficiente para resgate");
        }
        BigDecimal disponivel = saldoAtual != null ? saldoAtual : BigDecimal.ZERO;
        this.saldoEconomias = economias.subtract(valor);
        this.saldoAtual = disponivel.add(valor);
    }

    public void remover() {
        if ("S".equals(indDelete)) {
            throw new ContaFinanceiraInvalidaException("Conta financeira já foi removida");
        }
        this.indDelete = "S";
        this.deletedAt = OffsetDateTime.now();
        this.ativa = false;
    }

    /** Referência por identidade (id + code), usada quando só a chave da conta é conhecida. */
    public ContaFinanceira(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    public void validar() {
        if (usuarioId == null) {
            throw new ContaFinanceiraInvalidaException("Id do usuario é obrigatório");
        }
        if (usuarioCode == null) {
            throw new ContaFinanceiraInvalidaException("Code do usuario é obrigatório");
        }
        if (tipo == null) {
            throw new ContaFinanceiraInvalidaException("Tipo é obrigatório");
        }
        if (bancoId == null) {
            throw new ContaFinanceiraInvalidaException("BancoId é obrigatório");
        }
        if (bancoCode == null || bancoCode.isBlank()) {
            throw new ContaFinanceiraInvalidaException("BancoCode é obrigatório");
        }
        if (saldoInicial == null) {
            throw new ContaFinanceiraInvalidaException("Saldo inicial é obrigatório");
        }
    }
}
