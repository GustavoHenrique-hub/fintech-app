package com.enterprise.gustadev.fintech_app.application.banco;

import com.enterprise.gustadev.fintech_app.application.banco.usecase.DeletarBancoUseCase;
import com.enterprise.gustadev.fintech_app.domain.banco.exception.BancoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.banco.model.Banco;
import com.enterprise.gustadev.fintech_app.domain.banco.port.BancoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeletarBancoUseCaseTest {

    @Mock
    private BancoRepositoryPort repository;

    @InjectMocks
    private DeletarBancoUseCase useCase;

    @Test
    void executar_deveDelegarDeletarAoRepository_quandoBancoExiste() {
        Long id = 1L;
        String code = "NUBANK";
        Banco banco = new Banco(id, code, "Nubank", null, null, null);
        when(repository.buscarPorIdECode(id, code)).thenReturn(Optional.of(banco));

        useCase.executar(id, code);

        verify(repository).deletarPorId(id);
    }

    @Test
    void executar_deveLancarExcecao_quandoBancoNaoEncontrado() {
        Long id = 99L;
        String code = "XYZ999";
        when(repository.buscarPorIdECode(id, code)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(id, code))
                .isInstanceOf(BancoInvalidoException.class);

        verify(repository, never()).deletarPorId(id);
    }
}
