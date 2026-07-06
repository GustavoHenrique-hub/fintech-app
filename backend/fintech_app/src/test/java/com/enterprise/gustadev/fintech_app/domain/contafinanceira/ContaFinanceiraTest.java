package com.enterprise.gustadev.fintech_app.domain.contafinanceira;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContaFinanceiraTest {

    @Test
    void validar_devePassar_quandoDadosCorretos() {
        ContaFinanceira conta = new ContaFinanceira(
                1L, "USER01",TipoConta.corrente, 10L, "BNK001", BigDecimal.ZERO, false
        );
        assertThatCode(conta::validar).doesNotThrowAnyException();
    }

    @Test
    void validar_deveLancarExcecao_quandoUsuarioIdNulo() {
        ContaFinanceira conta = new ContaFinanceira(
                null, null, TipoConta.corrente, 10L, "BNK001", BigDecimal.ZERO, false
        );
        assertThatThrownBy(conta::validar)
                .isInstanceOf(ContaFinanceiraInvalidaException.class)
                .hasMessageContaining("UsuarioId");
    }

    @Test
    void validar_deveLancarExcecao_quandoTipoNulo() {
        ContaFinanceira conta = new ContaFinanceira(
                1L, "USER01", null,10L ,"BNK001", BigDecimal.ZERO, false
        );
        assertThatThrownBy(conta::validar)
                .isInstanceOf(ContaFinanceiraInvalidaException.class)
                .hasMessageContaining("Tipo");
    }

    @Test
    void validar_deveLancarExcecao_quandoBancoIdNulo() {
        ContaFinanceira conta = new ContaFinanceira(
                1L, "USER01",TipoConta.corrente, null, "BNK001", BigDecimal.ZERO, false
        );
        assertThatThrownBy(conta::validar)
                .isInstanceOf(ContaFinanceiraInvalidaException.class)
                .hasMessageContaining("BancoId");
    }

    @Test
    void validar_deveLancarExcecao_quandoBancoCodeVazio() {
        ContaFinanceira conta = new ContaFinanceira(
                1L, "USER01", TipoConta.corrente, 10L, "  ", BigDecimal.ZERO, false
        );
        assertThatThrownBy(conta::validar)
                .isInstanceOf(ContaFinanceiraInvalidaException.class)
                .hasMessageContaining("BancoCode");
    }

    @Test
    void validar_deveLancarExcecao_quandoSaldoInicialNulo() {
        ContaFinanceira conta = new ContaFinanceira(
                1L, "USER01", TipoConta.corrente, 10L, "BNK001", null, false
        );
        assertThatThrownBy(conta::validar)
                .isInstanceOf(ContaFinanceiraInvalidaException.class)
                .hasMessageContaining("Saldo");
    }
}
