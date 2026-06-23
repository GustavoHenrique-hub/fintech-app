package com.enterprise.gustadev.fintech_app.application.transacao;

import com.enterprise.gustadev.fintech_app.application.transacao.usecase.EstornarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusRevisaoTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoNaoEncontradaException;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstornarTransacaoUseCaseTest {

    @Mock
    private TransacaoRepositoryPort repository;

    @InjectMocks
    private EstornarTransacaoUseCase useCase;

    private Transacao original() {
        return new Transacao(10L, new Usuario(1L, "U1"), new ContaFinanceira(2L, "C1"), "N",
                TipoTransacao.GASTO, "desc", new BigDecimal("100.00"),
                LocalDate.now(), 5L, null, OrigemTransacao.manual,
                StatusRevisaoTransacao.EXTRAIDA, null, false,
                null, null, 1, null, OffsetDateTime.now(), null);
    }

    @Test
    void executar_deveInserirNovaLinhaComIndEstornoS_quandoNaoHaEstorno() {
        when(repository.buscarTransacao(anyLong(), anyString(), anyLong(), anyString(), anyLong(), anyString()))
                .thenReturn(Optional.of(original()));
        when(repository.buscarEstornoDe(10L)).thenReturn(Optional.empty());
        when(repository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        Transacao estorno = useCase.executar(10L, "ABC123", 1L, "U1", 2L, "C1");

        assertThat(estorno.getId()).isNull();
        assertThat(estorno.getCode()).isNotNull();
        assertThat(estorno.getIndEstorno()).isEqualTo("S");
        assertThat(estorno.getTransacaoEstornadaId()).isEqualTo(10L);
        assertThat(estorno.getValor()).isEqualByComparingTo("100.00");
        assertThat(estorno.getTipo()).isEqualTo(TipoTransacao.GASTO);
        verify(repository).salvar(any());
    }

    @Test
    void executar_deveRetornarEstornoExistente_semInserirDuplicado() {
        Transacao estornoExistente = original();
        estornoExistente.setIndEstorno("S");
        estornoExistente.setTransacaoEstornadaId(10L);

        when(repository.buscarTransacao(anyLong(), anyString(), anyLong(), anyString(), anyLong(), anyString()))
                .thenReturn(Optional.of(original()));
        when(repository.buscarEstornoDe(10L)).thenReturn(Optional.of(estornoExistente));

        Transacao resultado = useCase.executar(10L, "ABC123", 1L, "U1", 2L, "C1");

        assertThat(resultado).isSameAs(estornoExistente);
        verify(repository, never()).salvar(any());
    }

    @Test
    void executar_deveLancarExcecao_quandoTransacaoNaoEncontrada() {
        when(repository.buscarTransacao(anyLong(), anyString(), anyLong(), anyString(), anyLong(), anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(10L, "ABC123", 1L, "U1", 2L, "C1"))
                .isInstanceOf(TransacaoNaoEncontradaException.class);

        verify(repository, never()).salvar(any());
    }
}
