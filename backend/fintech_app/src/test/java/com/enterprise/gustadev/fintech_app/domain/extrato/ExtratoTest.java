package com.enterprise.gustadev.fintech_app.domain.extrato;

import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusExtrato;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExtratoTest {

    @Test
    void validar_devePassar_quandoDadosCorretos() {
        Extrato extrato = new Extrato(
                1L, "USR001",
                1L, "CTA001",
                "extrato.pdf", "file-uuid-abc", "abc123hash"
        );
        assertThatCode(extrato::validar).doesNotThrowAnyException();
    }

    @Test
    void validar_deveLancarExcecao_quandoUsuarioIdNulo() {
        Extrato extrato = new Extrato(null, "USR001", 1L, "CTA001", "f.pdf", "uuid", "hash");
        assertThatThrownBy(extrato::validar)
                .isInstanceOf(ExtratoInvalidoException.class)
                .hasMessageContaining("UsuarioId");
    }

    @Test
    void validar_deveLancarExcecao_quandoContaIdNula() {
        Extrato extrato = new Extrato(1L, "USR001", null, "CTA001", "f.pdf", "uuid", "hash");
        assertThatThrownBy(extrato::validar)
                .isInstanceOf(ExtratoInvalidoException.class)
                .hasMessageContaining("ContaId");
    }

    @Test
    void validar_deveLancarExcecao_quandoHashArquivoVazio() {
        Extrato extrato = new Extrato(1L, "USR001", 1L, "CTA001", "f.pdf", "uuid", "  ");
        assertThatThrownBy(extrato::validar)
                .isInstanceOf(ExtratoInvalidoException.class)
                .hasMessageContaining("Hash");
    }

    private Extrato extratoEm(StatusExtrato status) {
        Extrato extrato = new Extrato(1L, "USR001", 1L, "CTA001", "f.pdf", "uuid", "hash");
        extrato.setStatus(status);
        return extrato;
    }

    @ParameterizedTest
    @EnumSource(value = StatusExtrato.class, names = {"na_fila", "extraindo", "classificando", "reprocessando"})
    void cancelarProcessamento_deveIrParaErroClassificacao_quandoEmProcessamento(StatusExtrato status) {
        Extrato extrato = extratoEm(status);
        extrato.cancelarProcessamento();
        assertThat(extrato.getStatus()).isEqualTo(StatusExtrato.erro_classificacao);
        assertThat(extrato.emProcessamento()).isFalse();
    }

    @Test
    void cancelarProcessamento_deveLancarExcecao_quandoForaDoProcessamento() {
        Extrato extrato = extratoEm(StatusExtrato.pendente_revisao);
        assertThatThrownBy(extrato::cancelarProcessamento)
                .isInstanceOf(ExtratoInvalidoException.class)
                .hasMessageContaining("cancelar");
    }

    @ParameterizedTest
    @EnumSource(value = StatusExtrato.class, names = {"erro_formato", "erro_extracao", "erro_classificacao", "erro_timeout", "cancelado"})
    void prepararReenvio_deveVoltarAoProcessamento_quandoTerminouEmErro(StatusExtrato status) {
        Extrato extrato = extratoEm(status);
        int versaoAnterior = extrato.getVersao();

        extrato.prepararReenvio();

        assertThat(extrato.getStatus()).isEqualTo(StatusExtrato.reprocessando);
        assertThat(extrato.getVersao()).isEqualTo(versaoAnterior + 1);
    }

    @ParameterizedTest
    @EnumSource(value = StatusExtrato.class, names = {"na_fila", "pendente_revisao", "concluido"})
    void prepararReenvio_deveLancarExcecao_quandoNaoTerminouEmErro(StatusExtrato status) {
        Extrato extrato = extratoEm(status);
        assertThatThrownBy(extrato::prepararReenvio)
                .isInstanceOf(ExtratoInvalidoException.class)
                .hasMessageContaining("reenviar");
    }

    @Test
    void prepararReenvio_deveLancarExcecao_quandoErroJaGerouLancamentos() {
        Extrato extrato = extratoEm(StatusExtrato.erro_extracao);
        extrato.setTotalLancamentos(3);
        assertThatThrownBy(extrato::prepararReenvio).isInstanceOf(ExtratoInvalidoException.class);
    }
}
