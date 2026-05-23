package com.enterprise.gustadev.fintech_app.domain.extrato;

import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExtratoTest {

    @Test
    void validar_devePassar_quandoDadosCorretos() {
        Extrato extrato = new Extrato(
                1L, 1L,
                "extrato.pdf", "file-uuid-abc", "abc123hash"
        );
        assertThatCode(extrato::validar).doesNotThrowAnyException();
    }

    @Test
    void validar_deveLancarExcecao_quandoUsuarioIdNulo() {
        Extrato extrato = new Extrato(null, 1L, "f.pdf", "uuid", "hash");
        assertThatThrownBy(extrato::validar)
                .isInstanceOf(ExtratoInvalidoException.class)
                .hasMessageContaining("UsuarioId");
    }

    @Test
    void validar_deveLancarExcecao_quandoContaIdNula() {
        Extrato extrato = new Extrato(1L, null, "f.pdf", "uuid", "hash");
        assertThatThrownBy(extrato::validar)
                .isInstanceOf(ExtratoInvalidoException.class)
                .hasMessageContaining("ContaId");
    }

    @Test
    void validar_deveLancarExcecao_quandoHashArquivoVazio() {
        Extrato extrato = new Extrato(1L, 1L, "f.pdf", "uuid", "  ");
        assertThatThrownBy(extrato::validar)
                .isInstanceOf(ExtratoInvalidoException.class)
                .hasMessageContaining("Hash");
    }
}
