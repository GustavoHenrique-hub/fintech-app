package com.enterprise.gustadev.fintech_app.application.contafinanceira;

import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.ListarContasFinanceirasUseCase;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarContasFinanceirasUseCaseTest {

    @Mock
    private ContaFinanceiraRepositoryPort repository;

    @InjectMocks
    private ListarContasFinanceirasUseCase useCase;

    @Test
    void executar_deveRetornarListaDeContas() {
        Long usuarioId = 1L;
        ContaFinanceira conta = new ContaFinanceira(1L, usuarioId, "Nubank",
                TipoConta.corrente, "Nubank", BigDecimal.TEN, false, true, null, null);
        when(repository.listarPorUsuario(usuarioId)).thenReturn(List.of(conta));

        List<ContaFinanceira> resultado = useCase.executar(usuarioId);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Nubank");
        verify(repository).listarPorUsuario(usuarioId);
    }

    @Test
    void executar_deveRetornarListaVazia_quandoNaoHaContas() {
        Long usuarioId = 1L;
        when(repository.listarPorUsuario(usuarioId)).thenReturn(List.of());

        List<ContaFinanceira> resultado = useCase.executar(usuarioId);

        assertThat(resultado).isEmpty();
    }
}
