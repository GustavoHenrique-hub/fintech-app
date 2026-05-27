package com.enterprise.gustadev.fintech_app.application.categoria;

import com.enterprise.gustadev.fintech_app.application.categoria.usecase.CriarCategoriaUseCase;
import com.enterprise.gustadev.fintech_app.domain.categoria.exception.CategoriaInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarCategoriaUseCaseTest {

    @Mock
    private CategoriaRepositoryPort repository;

    @InjectMocks
    private CriarCategoriaUseCase useCase;

    @Test
    void executar_deveSalvarCategoria_quandoDadosValidos() {
        Categoria categoria = new Categoria("Pets", TipoCategoria.GASTO, "🐶", "#FF0000");
        when(repository.salvar(any())).thenReturn(categoria);

        useCase.executar(categoria);

        verify(repository).salvar(categoria);
    }

    @Test
    void executar_naoDeveSalvar_quandoNomeVazio() {
        Categoria categoriaInvalida = new Categoria("  ", TipoCategoria.GASTO, null, null);

        assertThatThrownBy(() -> useCase.executar(categoriaInvalida))
                .isInstanceOf(CategoriaInvalidaException.class);

        verify(repository, never()).salvar(any());
    }
}
