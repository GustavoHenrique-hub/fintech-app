package com.enterprise.gustadev.fintech_app.domain.categoria;

import com.enterprise.gustadev.fintech_app.domain.categoria.exception.CategoriaInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoriaTest {

    @Test
    void validar_devePassar_quandoCategoriaPersonalizadaComUsuario() {
        Categoria categoria = new Categoria(UUID.randomUUID(), "Pets", TipoCategoria.gasto, "🐶", "#FF0000");
        assertThatCode(categoria::validar).doesNotThrowAnyException();
    }

    @Test
    void validar_deveLancarExcecao_quandoNomeVazio() {
        Categoria categoria = new Categoria(UUID.randomUUID(), "  ", TipoCategoria.gasto, null, null);
        assertThatThrownBy(categoria::validar)
                .isInstanceOf(CategoriaInvalidaException.class)
                .hasMessageContaining("Nome");
    }

    @Test
    void validar_deveLancarExcecao_quandoTipoNulo() {
        Categoria categoria = new Categoria(UUID.randomUUID(), "Pets", null, null, null);
        assertThatThrownBy(categoria::validar)
                .isInstanceOf(CategoriaInvalidaException.class)
                .hasMessageContaining("Tipo");
    }

    @Test
    void validar_deveLancarExcecao_quandoNaoPadraoEUsuarioIdNulo() {
        Categoria categoria = new Categoria(null, "Pets", null, TipoCategoria.gasto, null, null, false, null, null);
        assertThatThrownBy(categoria::validar)
                .isInstanceOf(CategoriaInvalidaException.class)
                .hasMessageContaining("UsuarioId");
    }

    @Test
    void validar_devePassar_quandoCategoriaPadrao() {
        Categoria categoria = new Categoria(null, "Alimentação", null, TipoCategoria.gasto, "🍽️", "#EF4444", true, null, null);
        assertThatCode(categoria::validar).doesNotThrowAnyException();
    }
}
