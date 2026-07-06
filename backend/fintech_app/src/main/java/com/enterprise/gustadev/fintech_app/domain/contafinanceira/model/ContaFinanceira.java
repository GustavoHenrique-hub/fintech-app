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
    private Usuario usuario;
    private TipoConta tipo;
    private Long bancoId;
    private String bancoCode;
    private BigDecimal saldoInicial;
    private BigDecimal saldoAtual;
    private boolean padrao;
    private boolean ativa;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;
    private String indDelete;
    private OffsetDateTime deletedAt;

    public ContaFinanceira(Long id, Usuario usuario, TipoConta tipo,
                           Long bancoId, String bancoCode, BigDecimal saldoInicial, BigDecimal saldoAtual,
                           boolean padrao, boolean ativa, OffsetDateTime criadoEm, OffsetDateTime atualizadoEm,
                           String indDelete, OffsetDateTime deletedAt) {
        this.id = id;
        this.usuario = usuario;
        this.tipo = tipo;
        this.bancoId = bancoId;
        this.bancoCode = bancoCode;
        this.saldoInicial = saldoInicial;
        this.saldoAtual = saldoAtual;
        this.padrao = padrao;
        this.ativa = ativa;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.indDelete = indDelete != null ? indDelete : "N";
        this.deletedAt = deletedAt;
    }

    public ContaFinanceira(Usuario usuario, TipoConta tipo,
                           Long bancoId, String bancoCode, BigDecimal saldoInicial, boolean padrao) {
        this(null, usuario, tipo, bancoId, bancoCode, saldoInicial,
                saldoInicial != null ? saldoInicial : BigDecimal.ZERO,
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
        if (usuario == null) {
            throw new ContaFinanceiraInvalidaException("Usuario é obrigatório");
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
