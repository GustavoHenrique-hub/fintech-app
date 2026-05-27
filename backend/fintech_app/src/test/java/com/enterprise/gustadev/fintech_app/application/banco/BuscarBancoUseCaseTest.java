package com.enterprise.gustadev.fintech_app.application.banco;

import com.enterprise.gustadev.fintech_app.application.banco.usecase.BuscarBancoUseCase;
import com.enterprise.gustadev.fintech_app.domain.banco.exception.BancoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.banco.model.Banco;
import com.enterprise.gustadev.fintech_app.domain.banco.port.BancoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarBancoUseCaseTest {

    @Mock
    private BancoRepositoryPort repository;

    @InjectMocks
    private BuscarBancoUseCase useCase;

    @Test
    void executar_deveRetornarBanco_quandoEncontrado() {
        Long id = 1L;
        String code = "NUBANK";
        Banco banco = new Banco(id, code, "Nubank", null, "#8A05BE", null);
        when(repository.buscarPorIdECode(id, code)).thenReturn(Optional.of(banco));

        Banco resultado = useCase.executar(id, code);

        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getCode()).isEqualTo(code);
    }

    @Test
    void executar_deveLancarExcecao_quandoNaoEncontrado() {
        Long id = 99L;
        String code = "XYZ999";
        when(repository.buscarPorIdECode(id, code)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(id, code))
                .isInstanceOf(BancoInvalidoException.class)
                .hasMessageContaining(id.toString());
    }
}
