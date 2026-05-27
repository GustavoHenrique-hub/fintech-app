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
        Long id = 1L;
        String code = "ABC123";
        ContaFinanceira conta = new ContaFinanceira(id, 1L,
                TipoConta.corrente, 10L, "BNK001", BigDecimal.TEN, false, true, null, null);
        when(repository.buscarPorIdECode(id, code)).thenReturn(Optional.of(conta));

        ContaFinanceira resultado = useCase.executar(id, code);

        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getBancoId()).isEqualTo(10L);
    }

    @Test
    void executar_deveLancarExcecao_quandoNaoEncontrada() {
        Long id = 1L;
        String code = "XYZ999";
        when(repository.buscarPorIdECode(id, code)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(id, code))
                .isInstanceOf(ContaFinanceiraInvalidaException.class)
                .hasMessageContaining(id.toString());
    }
}
