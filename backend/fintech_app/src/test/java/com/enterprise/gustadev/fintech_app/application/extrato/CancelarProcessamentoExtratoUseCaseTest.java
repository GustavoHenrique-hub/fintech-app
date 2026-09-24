package com.enterprise.gustadev.fintech_app.application.extrato;

import com.enterprise.gustadev.fintech_app.application.extrato.usecase.CancelarProcessamentoExtratoUseCase;
import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusExtrato;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelarProcessamentoExtratoUseCaseTest {

    @Mock
    private ExtratoRepositoryPort repository;

    private CancelarProcessamentoExtratoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CancelarProcessamentoExtratoUseCase(repository);
    }

    private Extrato extratoEm(StatusExtrato status) {
        Extrato extrato = new Extrato(1L, "USR001", 1L, "CTA001", "extrato.pdf", "uuid", "hash");
        extrato.setId(12L);
        extrato.setStatus(status);
        return extrato;
    }

    @Test
    void executar_deveMoverExtratoNaFilaParaErroClassificacao() {
        Extrato extrato = extratoEm(StatusExtrato.na_fila);
        when(repository.buscarPorIdECode(12L, extrato.getCode())).thenReturn(Optional.of(extrato));
        when(repository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        Extrato resultado = useCase.executar(12L, extrato.getCode());

        assertThat(resultado.getStatus()).isEqualTo(StatusExtrato.erro_classificacao);
        verify(repository).salvar(extrato);
    }

    @Test
    void executar_deveRecusar_quandoExtratoJaSaiuDoProcessamento() {
        Extrato extrato = extratoEm(StatusExtrato.concluido);
        when(repository.buscarPorIdECode(12L, extrato.getCode())).thenReturn(Optional.of(extrato));

        assertThatThrownBy(() -> useCase.executar(12L, extrato.getCode()))
                .isInstanceOf(ExtratoInvalidoException.class);
        verify(repository, never()).salvar(any());
    }
}
