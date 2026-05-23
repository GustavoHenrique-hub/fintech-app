package com.enterprise.gustadev.fintech_app.application.contafinanceira;

import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.BuscarContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarContaFinanceiraUseCaseTest {

    @Mock
    private ContaFinanceiraRepositoryPort repository;

    @InjectMocks
    private BuscarContaFinanceiraUseCase useCase;

    @Test
    void executar_deveRetornarConta_quandoEncontrada() {
        UUID id = UUID.randomUUID();
        ContaFinanceira conta = new ContaFinanceira(id, UUID.randomUUID(), "Nubank",
                TipoConta.corrente, "Nubank", BigDecimal.TEN, false, true, null, null);
        when(repository.buscarPorId(id)).thenReturn(Optional.of(conta));

        ContaFinanceira resultado = useCase.executar(id);

        assertThat(resultado.getId()).isEqualTo(id);
    }

    @Test
    void executar_deveLancarExcecao_quandoNaoEncontrada() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(id))
                .isInstanceOf(ContaFinanceiraInvalidaException.class)
                .hasMessageContaining(id.toString());
    }
}
