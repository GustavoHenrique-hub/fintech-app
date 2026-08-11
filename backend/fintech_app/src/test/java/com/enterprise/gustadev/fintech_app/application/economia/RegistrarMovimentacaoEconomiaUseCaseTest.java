package com.enterprise.gustadev.fintech_app.application.economia;

import com.enterprise.gustadev.fintech_app.application.economia.usecase.RegistrarMovimentacaoEconomiaUseCase;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.economia.model.MovimentacaoEconomia;
import com.enterprise.gustadev.fintech_app.domain.economia.port.MovimentacaoEconomiaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoMovimentacaoEconomia;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarMovimentacaoEconomiaUseCaseTest {

    @Mock
    private MovimentacaoEconomiaRepositoryPort movimentacaoRepository;

    @Mock
    private ContaFinanceiraRepositoryPort contaRepository;

    @InjectMocks
    private RegistrarMovimentacaoEconomiaUseCase useCase;

    private ContaFinanceira conta(BigDecimal saldoAtual, BigDecimal saldoEconomias) {
        ContaFinanceira conta = new ContaFinanceira(
                1L, 1L, "USER01", TipoConta.corrente, 10L, "BNK001",
                new BigDecimal("500.00"), saldoAtual, saldoEconomias,
                false, true, OffsetDateTime.now(), null, "N", null);
        conta.setCode("ABC123");
        return conta;
    }

    @Test
    void executar_aporte_deveMoverSaldoESalvarMovimentacao_quandoSaldoSuficiente() {
        ContaFinanceira conta = conta(new BigDecimal("500.00"), new BigDecimal("100.00"));
        when(contaRepository.buscarPorIdECode(1L, "ABC123")).thenReturn(Optional.of(conta));
        when(contaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(movimentacaoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        MovimentacaoEconomia resultado = useCase.executar(
                1L, "ABC123", TipoMovimentacaoEconomia.APORTE, new BigDecimal("150.00"), "Reserva para viagem");

        assertThat(resultado.getTipo()).isEqualTo(TipoMovimentacaoEconomia.APORTE);
        assertThat(resultado.getValor()).isEqualByComparingTo("150.00");
        assertThat(resultado.getContaId()).isEqualTo(1L);
        assertThat(resultado.getContaCode()).isEqualTo("ABC123");
        assertThat(resultado.getCode()).isNotBlank();
        assertThat(resultado.getDescricao()).isEqualTo("Reserva para viagem");

        ArgumentCaptor<ContaFinanceira> contaCaptor = ArgumentCaptor.forClass(ContaFinanceira.class);
        verify(contaRepository).salvar(contaCaptor.capture());
        assertThat(contaCaptor.getValue().getSaldoAtual()).isEqualByComparingTo("350.00");
        assertThat(contaCaptor.getValue().getSaldoEconomias()).isEqualByComparingTo("250.00");
        verify(movimentacaoRepository).salvar(any());
    }

    @Test
    void executar_aporte_naoDeveSalvar_quandoSaldoInsuficiente() {
        ContaFinanceira conta = conta(new BigDecimal("50.00"), BigDecimal.ZERO);
        when(contaRepository.buscarPorIdECode(1L, "ABC123")).thenReturn(Optional.of(conta));

        assertThatThrownBy(() -> useCase.executar(
                1L, "ABC123", TipoMovimentacaoEconomia.APORTE, new BigDecimal("500.00"), null))
                .isInstanceOf(ContaFinanceiraInvalidaException.class)
                .hasMessageContaining("insuficiente");

        verify(contaRepository, never()).salvar(any());
        verify(movimentacaoRepository, never()).salvar(any());
    }

    @Test
    void executar_resgate_deveMoverSaldoESalvarMovimentacao_quandoEconomiasSuficientes() {
        ContaFinanceira conta = conta(new BigDecimal("100.00"), new BigDecimal("300.00"));
        when(contaRepository.buscarPorIdECode(1L, "ABC123")).thenReturn(Optional.of(conta));
        when(contaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(movimentacaoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        MovimentacaoEconomia resultado = useCase.executar(
                1L, "ABC123", TipoMovimentacaoEconomia.RESGATE, new BigDecimal("200.00"), null);

        assertThat(resultado.getTipo()).isEqualTo(TipoMovimentacaoEconomia.RESGATE);
        assertThat(resultado.getValor()).isEqualByComparingTo("200.00");

        ArgumentCaptor<ContaFinanceira> contaCaptor = ArgumentCaptor.forClass(ContaFinanceira.class);
        verify(contaRepository).salvar(contaCaptor.capture());
        assertThat(contaCaptor.getValue().getSaldoAtual()).isEqualByComparingTo("300.00");
        assertThat(contaCaptor.getValue().getSaldoEconomias()).isEqualByComparingTo("100.00");
        verify(movimentacaoRepository).salvar(any());
    }

    @Test
    void executar_resgate_naoDeveSalvar_quandoEconomiasInsuficientes() {
        ContaFinanceira conta = conta(new BigDecimal("100.00"), new BigDecimal("50.00"));
        when(contaRepository.buscarPorIdECode(1L, "ABC123")).thenReturn(Optional.of(conta));

        assertThatThrownBy(() -> useCase.executar(
                1L, "ABC123", TipoMovimentacaoEconomia.RESGATE, new BigDecimal("200.00"), null))
                .isInstanceOf(ContaFinanceiraInvalidaException.class)
                .hasMessageContaining("insuficiente");

        verify(contaRepository, never()).salvar(any());
        verify(movimentacaoRepository, never()).salvar(any());
    }

    @Test
    void executar_deveLancarExcecao_quandoContaNaoEncontrada() {
        when(contaRepository.buscarPorIdECode(any(), anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(
                999L, "XXXXXX", TipoMovimentacaoEconomia.APORTE, new BigDecimal("50.00"), null))
                .isInstanceOf(ContaFinanceiraInvalidaException.class)
                .hasMessageContaining("não encontrada");

        verify(movimentacaoRepository, never()).salvar(any());
    }
}
