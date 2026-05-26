package com.enterprise.gustadev.fintech_app.application.extrato;

import com.enterprise.gustadev.fintech_app.application.extrato.usecase.CriarExtratoUseCase;
import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusExtrato;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarExtratoUseCaseTest {

    @Mock
    private ExtratoRepositoryPort repository;

    @InjectMocks
    private CriarExtratoUseCase useCase;

    @Test
    void executar_deveSalvarExtrato_quandoHashInexistente() {
        Extrato extrato = new Extrato(1L, 1L, "nubank.pdf", "uuid-123", "hash-abc");
        Extrato salvo = new Extrato(1L, extrato.getUsuarioId(), extrato.getContaId(), null,
                extrato.getArquivoNome(), extrato.getArquivoUuid(), extrato.getHashArquivo(),
                null, null, null, null, null, StatusExtrato.upload_recebido,
                0, 0, 0, 0, 1, null, null);
        when(repository.buscarPorHash("hash-abc")).thenReturn(Optional.empty());
        when(repository.salvar(any())).thenReturn(salvo);

        useCase.executar(extrato);

        verify(repository).salvar(extrato);
    }

    @Test
    void executar_deveLancarExcecao_quandoHashJaExistente() {
        Extrato extrato = new Extrato(1L, 1L, "nubank.pdf", "uuid-123", "hash-dup");
        Extrato existente = new Extrato(1L, 1L, 1L, 1L,"nubank.pdf", "uuid-old", "hash-dup", null, null, null, null, null,
                StatusExtrato.concluido, 0, 0, 0, 0, 1, null, null);
        when(repository.buscarPorHash("hash-dup")).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> useCase.executar(extrato))
                .isInstanceOf(ExtratoInvalidoException.class)
                .hasMessageContaining("duplicado");

        verify(repository, never()).salvar(any());
    }

    @Test
    void executar_deveLancarExcecao_quandoDadosInvalidos() {
        Extrato extratoInvalido = new Extrato(null, 1L, "f.pdf", "uuid", "hash");

        assertThatThrownBy(() -> useCase.executar(extratoInvalido))
                .isInstanceOf(ExtratoInvalidoException.class);

        verify(repository, never()).buscarPorHash(anyString());
        verify(repository, never()).salvar(any());
    }
}
