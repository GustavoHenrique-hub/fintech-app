package com.enterprise.gustadev.fintech_app.application.banco;

import com.enterprise.gustadev.fintech_app.application.banco.usecase.ListarBancosUseCase;
import com.enterprise.gustadev.fintech_app.domain.banco.model.Banco;
import com.enterprise.gustadev.fintech_app.domain.banco.port.BancoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarBancosUseCaseTest {

    @Mock
    private BancoRepositoryPort repository;

    @InjectMocks
    private ListarBancosUseCase useCase;

    @Test
    void executar_deveRetornarTodosOsBancos() {
        Banco nubank = new Banco(1L, "NUBANK", "Nubank", null, "#8A05BE", null);
        Banco itau = new Banco(2L, "ITAU01", "Itaú", null, "#EC7000", null);
        when(repository.listarTodos()).thenReturn(List.of(nubank, itau));

        List<Banco> resultado = useCase.executar();

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(Banco::getNome).containsExactly("Nubank", "Itaú");
        verify(repository).listarTodos();
    }

    @Test
    void executar_deveRetornarListaVazia_quandoNenhumBancoCadastrado() {
        when(repository.listarTodos()).thenReturn(List.of());

        assertThat(useCase.executar()).isEmpty();
    }
}
