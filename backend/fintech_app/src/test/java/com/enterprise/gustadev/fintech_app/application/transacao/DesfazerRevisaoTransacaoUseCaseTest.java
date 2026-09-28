package com.enterprise.gustadev.fintech_app.application.transacao;

import com.enterprise.gustadev.fintech_app.application.classificacao.CalibradorConfiancaIa;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.DesfazerRevisaoTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.domain.classificacao.model.AprendizadoClassificacao;
import com.enterprise.gustadev.fintech_app.domain.classificacao.port.AprendizadoClassificacaoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusExtrato;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusRevisaoTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DesfazerRevisaoTransacaoUseCaseTest {

    @Mock
    private TransacaoRepositoryPort transacaoRepository;
    @Mock
    private ExtratoRepositoryPort extratoRepository;
    @Mock
    private ContaFinanceiraRepositoryPort contaRepository;
    @Mock
    private AprendizadoClassificacaoRepositoryPort aprendizadoRepository;

    private DesfazerRevisaoTransacaoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DesfazerRevisaoTransacaoUseCase(transacaoRepository, extratoRepository, contaRepository,
                new CalibradorConfiancaIa(aprendizadoRepository));
    }

    private Transacao gastoConfirmado() {
        ContaFinanceira ref = new ContaFinanceira(2L, "CTA001");
        ref.setUsuarioId(1L);
        Transacao transacao = new Transacao(10L, ref, "N", "Supermercado", new BigDecimal("-150.50"),
                LocalDate.now(), 7L, "CAT007", null, OrigemTransacao.importado,
                StatusRevisaoTransacao.CONFIRMADA, (short) 90, false, null, null, 1,
                OffsetDateTime.now(), null, null);
        transacao.setCode("TRX001");
        transacao.setCategoriaTipo(TipoCategoria.AMBOS);
        transacao.setSaldoAplicado(true);
        transacao.setExtratoId(99L);
        return transacao;
    }

    private ContaFinanceira conta() {
        return new ContaFinanceira(2L, 1L, "USR001", TipoConta.corrente, 10L, "BCO001",
                new BigDecimal("1000.00"), new BigDecimal("850.00"), new BigDecimal("0.00"),
                false, true, OffsetDateTime.now(), null, "N", null);
    }

    private Extrato extratoConcluido() {
        Extrato extrato = new Extrato(1L, "USR001", 2L, "CTA001", "extrato.pdf", "uuid", "hash");
        extrato.setId(99L);
        extrato.setTotalLancamentos(1);
        extrato.setLancamentosConfirmados(1);
        extrato.setStatus(StatusExtrato.concluido);
        return extrato;
    }

    @Test
    void executar_deveVoltarParaPendente_tirarDoSaldo_eBaixarAConfianca() {
        AprendizadoClassificacao aprendizado = new AprendizadoClassificacao(1L, "supermercado", TipoTransacao.GASTO);
        aprendizado.registrarAcerto(TipoTransacao.GASTO, null, null);

        when(transacaoRepository.buscarPorIdECode(10L, "TRX001")).thenReturn(Optional.of(gastoConfirmado()));
        when(transacaoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(contaRepository.buscarPorId(2L)).thenReturn(Optional.of(conta()));
        when(contaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(extratoRepository.buscarPorId(99L)).thenReturn(Optional.of(extratoConcluido()));
        when(aprendizadoRepository.buscarPorUsuarioEChave(1L, "supermercado")).thenReturn(Optional.of(aprendizado));

        Transacao resultado = useCase.executar(10L, "TRX001");

        assertThat(resultado.getStatusRevisao()).isEqualTo(StatusRevisaoTransacao.PENDENTE_REVISAO);
        assertThat(resultado.saldoJaAplicado()).isFalse();
        assertThat(resultado.getConfiancaIa()).isEqualTo((short) 75);

        ArgumentCaptor<ContaFinanceira> contaSalva = ArgumentCaptor.forClass(ContaFinanceira.class);
        verify(contaRepository).salvar(contaSalva.capture());
        assertThat(contaSalva.getValue().getSaldoAtual()).isEqualByComparingTo("1000.50");

        ArgumentCaptor<Extrato> extratoSalvo = ArgumentCaptor.forClass(Extrato.class);
        verify(extratoRepository).salvar(extratoSalvo.capture());
        assertThat(extratoSalvo.getValue().getLancamentosPendentes()).isEqualTo(1);
        assertThat(extratoSalvo.getValue().getStatus()).isEqualTo(StatusExtrato.parcialmente_revisado);

        assertThat(aprendizado.getErros()).isEqualTo(1);
        assertThat(aprendizado.ajuste()).isNegative();
        verify(aprendizadoRepository).salvar(aprendizado);
    }

    @Test
    void executar_deveRecusar_quandoTransacaoNaoEstaConfirmada() {
        Transacao pendente = gastoConfirmado();
        pendente.setStatusRevisao(StatusRevisaoTransacao.PENDENTE_REVISAO);
        when(transacaoRepository.buscarPorIdECode(10L, "TRX001")).thenReturn(Optional.of(pendente));

        assertThatThrownBy(() -> useCase.executar(10L, "TRX001"))
                .isInstanceOf(TransacaoInvalidaException.class);
        verify(transacaoRepository, never()).salvar(any());
    }
}
