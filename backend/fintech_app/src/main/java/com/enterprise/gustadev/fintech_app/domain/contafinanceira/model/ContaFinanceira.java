package com.enterprise.gustadev.fintech_app.domain.contafinanceira.model;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import com.enterprise.gustadev.fintech_app.domain.shared.util.CodeGenerator;
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
    private TipoConta tipo;
    private Long bancoId;
    private String bancoCode;
    private BigDecimal saldoInicial;
    private boolean padrao;
    private boolean ativa;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;

    public ContaFinanceira(Long id, Long usuarioId, TipoConta tipo,
                           Long bancoId, String bancoCode, BigDecimal saldoInicial, boolean padrao,
                           boolean ativa, OffsetDateTime criadoEm, OffsetDateTime atualizadoEm) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.bancoId = bancoId;
        this.bancoCode = bancoCode;
        this.saldoInicial = saldoInicial;
        this.padrao = padrao;
        this.ativa = ativa;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public ContaFinanceira(Long usuarioId, TipoConta tipo,
                           Long bancoId, String bancoCode, BigDecimal saldoInicial, boolean padrao) {
        this(null, usuarioId, tipo, bancoId, bancoCode, saldoInicial, padrao, true, OffsetDateTime.now(), null);
        this.code = CodeGenerator.gerar();
    }

    /** Referência por identidade (id + code), usada quando só a chave da conta é conhecida. */
    public ContaFinanceira(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    public void validar() {
        if (usuarioId == null) {
            throw new ContaFinanceiraInvalidaException("UsuarioId é obrigatório");
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
