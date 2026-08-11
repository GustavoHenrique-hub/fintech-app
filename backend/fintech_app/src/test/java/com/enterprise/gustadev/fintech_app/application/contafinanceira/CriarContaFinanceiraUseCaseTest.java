package com.enterprise.gustadev.fintech_app.application.contafinanceira;

import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.CriarContaFinanceiraUseCase;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarContaFinanceiraUseCaseTest {

    @Mock
    private ContaFinanceiraRepositoryPort repository;

    @InjectMocks
    private CriarContaFinanceiraUseCase useCase;

    @Test
    void executar_deveSalvarERetornarConta_quandoDadosValidos() {
        ContaFinanceira conta = new ContaFinanceira(
                1L, "USER01", TipoConta.corrente, 10L, "BNK001", new BigDecimal("1000.00"), false
        );
        Long idGerado = 1L;
        ContaFinanceira contaSalva = new ContaFinanceira(idGerado, conta.getUsuarioId(), conta.getUsuarioCode(),
                conta.getTipo(), conta.getBancoId(), conta.getBancoCode(),
                conta.getSaldoInicial(), conta.getSaldoInicial(), false, true, null, null, null, null);
        when(repository.salvar(any())).thenReturn(contaSalva);

        ContaFinanceira resultado = useCase.executar(conta);

        assertThat(resultado.getId()).isEqualTo(idGerado);
        assertThat(resultado.getBancoId()).isEqualTo(10L);
        assertThat(resultado.getBancoCode()).isEqualTo("BNK001");
        verify(repository).salvar(conta);
    }

    @Test
    void executar_naoDeveChamarRepository_quandoDadosInvalidos() {
        ContaFinanceira contaInvalida = new ContaFinanceira(
                null, null, TipoConta.corrente, null, null, BigDecimal.ZERO, false
        );

        assertThatThrownBy(() -> useCase.executar(contaInvalida))
                .isInstanceOf(ContaFinanceiraInvalidaException.class);

        verify(repository, never()).salvar(any());
    }
}
