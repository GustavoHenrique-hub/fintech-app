package com.enterprise.gustadev.fintech_app.application.banco;

import com.enterprise.gustadev.fintech_app.application.banco.usecase.CriarBancoUseCase;
import com.enterprise.gustadev.fintech_app.domain.banco.exception.BancoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.banco.model.Banco;
import com.enterprise.gustadev.fintech_app.domain.banco.port.BancoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarBancoUseCaseTest {

    @Mock
    private BancoRepositoryPort repository;

    @InjectMocks
    private CriarBancoUseCase useCase;

    @Test
    void executar_deveSalvarERetornarBanco_quandoDadosValidos() {
        Banco banco = new Banco("Nubank", "Banco digital", "#8A05BE", "nubank-icon");
        Banco salvo = new Banco(1L, banco.getCode(), banco.getNome(),
                banco.getDescricao(), banco.getCorHex(), banco.getIcone());
        when(repository.salvar(any())).thenReturn(salvo);

        Banco resultado = useCase.executar(banco);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getCode()).isEqualTo(banco.getCode());
        verify(repository).salvar(banco);
    }

    @Test
    void executar_naoDeveChamarRepository_quandoNomeVazio() {
        Banco invalido = new Banco("  ", null, null, null);

        assertThatThrownBy(() -> useCase.executar(invalido))
                .isInstanceOf(BancoInvalidoException.class);

        verify(repository, never()).salvar(any());
    }
}
