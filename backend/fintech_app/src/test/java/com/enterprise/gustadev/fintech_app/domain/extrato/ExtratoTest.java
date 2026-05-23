package com.enterprise.gustadev.fintech_app.domain.extrato;

import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExtratoTest {

    @Test
    void validar_devePassar_quandoDadosCorretos() {
        Extrato extrato = new Extrato(
                UUID.randomUUID(), UUID.randomUUID(),
                "extrato.pdf", UUID.randomUUID().toString(), "abc123hash"
        );
        assertThatCode(extrato::validar).doesNotThrowAnyException();
    }

    @Test
    void validar_deveLancarExcecao_quandoUsuarioIdNulo() {
        Extrato extrato = new Extrato(null, UUID.randomUUID(), "f.pdf", "uuid", "hash");
        assertThatThrownBy(extrato::validar)
                .isInstanceOf(ExtratoInvalidoException.class)
                .hasMessageContaining("UsuarioId");
    }

    @Test
    void validar_deveLancarExcecao_quandoContaIdNula() {
        Extrato extrato = new Extrato(UUID.randomUUID(), null, "f.pdf", "uuid", "hash");
        assertThatThrownBy(extrato::validar)
                .isInstanceOf(ExtratoInvalidoException.class)
                .hasMessageContaining("ContaId");
    }

    @Test
    void validar_deveLancarExcecao_quandoHashArquivoVazio() {
        Extrato extrato = new Extrato(UUID.randomUUID(), UUID.randomUUID(), "f.pdf", "uuid", "  ");
        assertThatThrownBy(extrato::validar)
                .isInstanceOf(ExtratoInvalidoException.class)
                .hasMessageContaining("Hash");
    }
}
