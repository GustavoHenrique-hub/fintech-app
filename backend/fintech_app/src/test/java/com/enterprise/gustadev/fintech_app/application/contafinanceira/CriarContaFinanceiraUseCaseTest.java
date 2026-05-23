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
                1L, "Nubank", TipoConta.corrente, "Nubank", new BigDecimal("1000.00"), false
        );
        Long idGerado = 1L;
        ContaFinanceira contaSalva = new ContaFinanceira(idGerado, conta.getUsuarioId(), conta.getNome(),
                conta.getTipo(), conta.getBanco(), conta.getSaldoInicial(), false, true, null, null);
        when(repository.salvar(any())).thenReturn(contaSalva);

        ContaFinanceira resultado = useCase.executar(conta);

        assertThat(resultado.getId()).isEqualTo(idGerado);
        assertThat(resultado.getNome()).isEqualTo("Nubank");
        verify(repository).salvar(conta);
    }

    @Test
    void executar_naoDeveChamarRepository_quandoDadosInvalidos() {
        ContaFinanceira contaInvalida = new ContaFinanceira(
                null, "  ", TipoConta.corrente, null, BigDecimal.ZERO, false
        );

        assertThatThrownBy(() -> useCase.executar(contaInvalida))
                .isInstanceOf(ContaFinanceiraInvalidaException.class);

        verify(repository, never()).salvar(any());
    }
}
