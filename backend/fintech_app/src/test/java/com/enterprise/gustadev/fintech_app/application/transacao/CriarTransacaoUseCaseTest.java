package com.enterprise.gustadev.fintech_app.application.transacao;

import com.enterprise.gustadev.fintech_app.application.transacao.usecase.CriarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusRevisaoTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarTransacaoUseCaseTest {

    @Mock
    private TransacaoRepositoryPort repository;

    @Mock
    private ContaFinanceiraRepositoryPort contaRepository;

    @InjectMocks
    private CriarTransacaoUseCase useCase;

    private ContaFinanceira contaComSaldo(Long id) {
        return new ContaFinanceira(id, 1L, "USER01",TipoConta.corrente, 10L, "BNK001",
                new BigDecimal("500.00"), new BigDecimal("500.00"),
                false, true, OffsetDateTime.now(), null, "N", null);
    }

    @Test
    void executar_deveSalvarTransacaoEAtualizarSaldo_quandoDadosValidos() {
        Long contaId = 1L;
        Transacao transacao = new Transacao(
                new ContaFinanceira(contaId, "C1"), TipoTransacao.GASTO,
                new BigDecimal("150.00"), LocalDate.now(), 1L, "CAT001", OrigemTransacao.manual
        );
        Transacao salva = new Transacao(1L, new ContaFinanceira(contaId, "C1"), "N",
                TipoTransacao.GASTO, null, new BigDecimal("150.00"),
                LocalDate.now(), 1L, "CAT001", null, OrigemTransacao.manual,
                StatusRevisaoTransacao.EXTRAIDA, null, false, null, null, 1, null, null, null);
        when(repository.salvar(any())).thenReturn(salva);
        when(contaRepository.buscarPorId(contaId)).thenReturn(Optional.of(contaComSaldo(contaId)));
        when(contaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        Transacao resultado = useCase.executar(transacao);

        assertThat(resultado.getId()).isNotNull();
        assertThat(resultado.getValor()).isEqualByComparingTo("150.00");
        verify(repository).salvar(transacao);
        verify(contaRepository).buscarPorId(contaId);
        verify(contaRepository).salvar(any());
    }

    @Test
    void executar_naoDeveSalvar_quandoValorInvalido() {
        Transacao transacaoInvalida = new Transacao(
                new ContaFinanceira(1L, "C1"), TipoTransacao.GASTO,
                BigDecimal.ZERO, LocalDate.now(), 1L, "CAT001", OrigemTransacao.manual
        );

        assertThatThrownBy(() -> useCase.executar(transacaoInvalida))
                .isInstanceOf(TransacaoInvalidaException.class);

        verify(repository, never()).salvar(any());
        verify(contaRepository, never()).buscarPorId(anyLong());
    }
}
