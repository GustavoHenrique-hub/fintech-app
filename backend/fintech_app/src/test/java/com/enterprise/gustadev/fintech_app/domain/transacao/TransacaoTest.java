package com.enterprise.gustadev.fintech_app.domain.transacao;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransacaoTest {

    @Test
    void validar_devePassar_quandoDadosCorretos() {
        Transacao transacao = new Transacao(
                new ContaFinanceira(1L, "C1"),
                TipoTransacao.GASTO, new BigDecimal("50.00"),
                LocalDate.now(), 1L, "CAT001", OrigemTransacao.manual
        );
        assertThatCode(transacao::validar).doesNotThrowAnyException();
    }

    @Test
    void validar_deveLancarExcecao_quandoContaNula() {
        Transacao transacao = new Transacao(
                null,
                TipoTransacao.GASTO, new BigDecimal("50.00"),
                LocalDate.now(), 1L, "CAT001", OrigemTransacao.manual
        );
        assertThatThrownBy(transacao::validar)
                .isInstanceOf(TransacaoInvalidaException.class)
                .hasMessageContaining("Conta");
    }

    @Test
    void validar_deveLancarExcecao_quandoValorZero() {
        Transacao transacao = new Transacao(
                new ContaFinanceira(1L, "C1"),
                TipoTransacao.GASTO, BigDecimal.ZERO,
                LocalDate.now(), 1L, "CAT001", OrigemTransacao.manual
        );
        assertThatThrownBy(transacao::validar)
                .isInstanceOf(TransacaoInvalidaException.class)
                .hasMessageContaining("Valor");
    }

    @Test
    void validar_deveLancarExcecao_quandoValorNegativo() {
        Transacao transacao = new Transacao(
                new ContaFinanceira(1L, "C1"),
                TipoTransacao.GASTO, new BigDecimal("-10.00"),
                LocalDate.now(), 1L, "CAT001", OrigemTransacao.manual
        );
        assertThatThrownBy(transacao::validar)
                .isInstanceOf(TransacaoInvalidaException.class)
                .hasMessageContaining("Valor");
    }

    @Test
    void validar_deveLancarExcecao_quandoDataNula() {
        Transacao transacao = new Transacao(
                new ContaFinanceira(1L, "C1"),
                TipoTransacao.RECEITA, new BigDecimal("100.00"),
                null, 1L, "CAT001", OrigemTransacao.manual
        );
        assertThatThrownBy(transacao::validar)
                .isInstanceOf(TransacaoInvalidaException.class)
                .hasMessageContaining("Data");
    }

    @Test
    void validar_deveLancarExcecao_quandoOrigemNula() {
        Transacao transacao = new Transacao(
                new ContaFinanceira(1L, "C1"),
                TipoTransacao.RECEITA, new BigDecimal("100.00"),
                LocalDate.now(), 1L, "CAT001", null
        );
        assertThatThrownBy(transacao::validar)
                .isInstanceOf(TransacaoInvalidaException.class)
                .hasMessageContaining("Origem");
    }
}
