package com.enterprise.gustadev.fintech_app.domain.banco;

import com.enterprise.gustadev.fintech_app.domain.banco.exception.BancoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.banco.model.Banco;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BancoTest {

    @Test
    void construtor_deveGerarCodeAutomaticamente() {
        Banco banco = new Banco("Nubank", "Banco digital", "#8A05BE", "nubank-icon");
        assertThat(banco.getCode()).hasSize(6);
        assertThat(banco.getId()).isNull();
    }

    @Test
    void validar_devePassar_quandoNomeInformado() {
        Banco banco = new Banco("Itaú", null, null, null);
        assertThatCode(banco::validar).doesNotThrowAnyException();
    }

    @Test
    void validar_deveLancarExcecao_quandoNomeVazio() {
        Banco banco = new Banco("  ", "Desc", "#000000", "icon");
        assertThatThrownBy(banco::validar)
                .isInstanceOf(BancoInvalidoException.class)
                .hasMessageContaining("Nome");
    }

    @Test
    void validar_deveLancarExcecao_quandoNomeNulo() {
        Banco banco = new Banco(null, null, null, null);
        assertThatThrownBy(banco::validar)
                .isInstanceOf(BancoInvalidoException.class)
                .hasMessageContaining("Nome");
    }
}
