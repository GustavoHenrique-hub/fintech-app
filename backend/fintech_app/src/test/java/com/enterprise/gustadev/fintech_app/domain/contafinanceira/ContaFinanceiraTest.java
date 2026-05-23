package com.enterprise.gustadev.fintech_app.domain.contafinanceira;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

class ContaFinanceiraTest {

    @Test
    void validar_devePassar_quandoDadosCorretos() {
        ContaFinanceira conta = new ContaFinanceira(
                1L, "Nubank Conta", TipoConta.corrente, "Nubank", BigDecimal.ZERO, false
        );
        assertThatCode(conta::validar).doesNotThrowAnyException();
    }

    @Test
    void validar_deveLancarExcecao_quandoUsuarioIdNulo() {
        ContaFinanceira conta = new ContaFinanceira(
                null, "Conta", TipoConta.corrente, null, BigDecimal.ZERO, false
        );
        assertThatThrownBy(conta::validar)
                .isInstanceOf(ContaFinanceiraInvalidaException.class)
                .hasMessageContaining("UsuarioId");
    }

    @Test
    void validar_deveLancarExcecao_quandoNomeVazio() {
        ContaFinanceira conta = new ContaFinanceira(
                1L, "  ", TipoConta.corrente, null, BigDecimal.ZERO, false
        );
        assertThatThrownBy(conta::validar)
                .isInstanceOf(ContaFinanceiraInvalidaException.class)
                .hasMessageContaining("Nome");
    }

    @Test
    void validar_deveLancarExcecao_quandoTipoNulo() {
        ContaFinanceira conta = new ContaFinanceira(
                1L, "Conta", null, null, BigDecimal.ZERO, false
        );
        assertThatThrownBy(conta::validar)
                .isInstanceOf(ContaFinanceiraInvalidaException.class)
                .hasMessageContaining("Tipo");
    }

    @Test
    void validar_deveLancarExcecao_quandoSaldoInicialNulo() {
        ContaFinanceira conta = new ContaFinanceira(
                1L, "Conta", TipoConta.corrente, null, null, false
        );
        assertThatThrownBy(conta::validar)
                .isInstanceOf(ContaFinanceiraInvalidaException.class)
                .hasMessageContaining("Saldo");
    }
}
