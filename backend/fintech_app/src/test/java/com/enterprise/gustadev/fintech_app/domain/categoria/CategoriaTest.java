package com.enterprise.gustadev.fintech_app.domain.categoria;

import com.enterprise.gustadev.fintech_app.domain.categoria.exception.CategoriaInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoriaTest {

    @Test
    void validar_devePassar_quandoDadosValidos() {
        Categoria categoria = new Categoria("Pets", TipoCategoria.GASTO, "🐶", "#FF0000");
        assertThatCode(categoria::validar).doesNotThrowAnyException();
    }

    @Test
    void validar_deveLancarExcecao_quandoNomeVazio() {
        Categoria categoria = new Categoria("  ", TipoCategoria.GASTO, null, null);
        assertThatThrownBy(categoria::validar)
                .isInstanceOf(CategoriaInvalidaException.class)
                .hasMessageContaining("Nome");
    }

    @Test
    void validar_deveLancarExcecao_quandoTipoNulo() {
        Categoria categoria = new Categoria("Pets", null, null, null);
        assertThatThrownBy(categoria::validar)
                .isInstanceOf(CategoriaInvalidaException.class)
                .hasMessageContaining("Tipo");
    }

    @Test
    void validar_devePassar_quandoCategoriaPadrao() {
        Categoria categoria = new Categoria(null, "Alimentação", TipoCategoria.GASTO, "🍽️", "#EF4444", true, null);
        assertThatCode(categoria::validar).doesNotThrowAnyException();
    }
}
