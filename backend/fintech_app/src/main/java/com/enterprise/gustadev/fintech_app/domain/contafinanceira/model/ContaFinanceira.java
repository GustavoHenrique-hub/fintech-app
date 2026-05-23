package com.enterprise.gustadev.fintech_app.domain.contafinanceira.model;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
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
    private String nome;
    private TipoConta tipo;
    private String banco;
    private BigDecimal saldoInicial;
    private boolean padrao;
    private boolean ativa;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;

    public ContaFinanceira(Long id, Long usuarioId, String nome, TipoConta tipo,
                           String banco, BigDecimal saldoInicial, boolean padrao,
                           boolean ativa, OffsetDateTime criadoEm, OffsetDateTime atualizadoEm) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.tipo = tipo;
        this.banco = banco;
        this.saldoInicial = saldoInicial;
        this.padrao = padrao;
        this.ativa = ativa;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public ContaFinanceira(Long usuarioId, String nome, TipoConta tipo,
                           String banco, BigDecimal saldoInicial, boolean padrao) {
        this(null, usuarioId, nome, tipo, banco, saldoInicial, padrao, true, null, null);
    }

    public void validar() {
        if (usuarioId == null) {
            throw new ContaFinanceiraInvalidaException("UsuarioId é obrigatório");
        }
        if (nome == null || nome.isBlank()) {
            throw new ContaFinanceiraInvalidaException("Nome é obrigatório");
        }
        if (tipo == null) {
            throw new ContaFinanceiraInvalidaException("Tipo é obrigatório");
        }
        if (saldoInicial == null) {
            throw new ContaFinanceiraInvalidaException("Saldo inicial é obrigatório");
        }
    }
}
