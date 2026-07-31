package com.enterprise.gustadev.fintech_app.domain.contafinanceira;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
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

    private ContaFinanceira contaComSaldos(BigDecimal saldoAtual, BigDecimal saldoEconomias) {
        ContaFinanceira conta = new ContaFinanceira(
                1L, "USER01", TipoConta.corrente, 10L, "BNK001", saldoAtual, false);
        conta.setSaldoAtual(saldoAtual);
        conta.setSaldoEconomias(saldoEconomias);
        return conta;
    }

    @Test
    void aportarEconomia_deveMoverValorDoSaldoAtualParaEconomias_quandoSaldoSuficiente() {
        ContaFinanceira conta = contaComSaldos(new BigDecimal("500.00"), new BigDecimal("100.00"));

        conta.aportarEconomia(new BigDecimal("150.00"));

        assertThat(conta.getSaldoAtual()).isEqualByComparingTo("350.00");
        assertThat(conta.getSaldoEconomias()).isEqualByComparingTo("250.00");
    }

    @Test
    void aportarEconomia_deveLancarExcecao_quandoSaldoInsuficiente() {
        ContaFinanceira conta = contaComSaldos(new BigDecimal("50.00"), new BigDecimal("0.00"));

        assertThatThrownBy(() -> conta.aportarEconomia(new BigDecimal("150.00")))
                .isInstanceOf(ContaFinanceiraInvalidaException.class)
                .hasMessageContaining("insuficiente");
        assertThat(conta.getSaldoAtual()).isEqualByComparingTo("50.00");
        assertThat(conta.getSaldoEconomias()).isEqualByComparingTo("0.00");
    }

    @Test
    void aportarEconomia_deveLancarExcecao_quandoValorNaoPositivo() {
        ContaFinanceira conta = contaComSaldos(new BigDecimal("500.00"), BigDecimal.ZERO);

        assertThatThrownBy(() -> conta.aportarEconomia(BigDecimal.ZERO))
                .isInstanceOf(ContaFinanceiraInvalidaException.class)
                .hasMessageContaining("positivo");
        assertThatThrownBy(() -> conta.aportarEconomia(new BigDecimal("-10.00")))
                .isInstanceOf(ContaFinanceiraInvalidaException.class);
    }

    @Test
    void resgatarEconomia_deveMoverValorDeEconomiasParaSaldoAtual_quandoSuficiente() {
        ContaFinanceira conta = contaComSaldos(new BigDecimal("100.00"), new BigDecimal("300.00"));

        conta.resgatarEconomia(new BigDecimal("200.00"));

        assertThat(conta.getSaldoAtual()).isEqualByComparingTo("300.00");
        assertThat(conta.getSaldoEconomias()).isEqualByComparingTo("100.00");
    }

    @Test
    void resgatarEconomia_deveLancarExcecao_quandoEconomiasInsuficientes() {
        ContaFinanceira conta = contaComSaldos(new BigDecimal("100.00"), new BigDecimal("50.00"));

        assertThatThrownBy(() -> conta.resgatarEconomia(new BigDecimal("200.00")))
                .isInstanceOf(ContaFinanceiraInvalidaException.class)
                .hasMessageContaining("insuficiente");
        assertThat(conta.getSaldoAtual()).isEqualByComparingTo("100.00");
        assertThat(conta.getSaldoEconomias()).isEqualByComparingTo("50.00");
    }
}
