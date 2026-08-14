package com.enterprise.gustadev.fintech_app.application.transacao;

import com.enterprise.gustadev.fintech_app.application.transacao.usecase.ConfirmarRevisaoTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.economia.model.MovimentacaoEconomia;
import com.enterprise.gustadev.fintech_app.domain.economia.port.MovimentacaoEconomiaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.DestinoRevisaoLancamento;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusRevisaoTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoMovimentacaoEconomia;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfirmarRevisaoTransacaoUseCaseTest {

    @Mock
    private TransacaoRepositoryPort transacaoRepository;
    @Mock
    private ExtratoRepositoryPort extratoRepository;
    @Mock
    private ContaFinanceiraRepositoryPort contaRepository;
    @Mock
    private CategoriaRepositoryPort categoriaRepository;
    @Mock
    private MovimentacaoEconomiaRepositoryPort movimentacaoEconomiaRepository;

    @InjectMocks
    private ConfirmarRevisaoTransacaoUseCase useCase;

    /** Lançamento importado: nasce na categoria genérica (AMBOS), negativo = gasto. */
    private Transacao lancamentoPendente(BigDecimal valor) {
        Transacao transacao = new Transacao(10L, new ContaFinanceira(2L, "CTA001"), "N",
                "Supermercado", valor, LocalDate.now(), 7L, "CAT007", null,
                OrigemTransacao.importado, StatusRevisaoTransacao.PENDENTE_REVISAO, null, false,
                null, null, 1, OffsetDateTime.now(), null, null);
        transacao.setCode("TRX001");
        transacao.setCategoriaTipo(TipoCategoria.AMBOS);
        transacao.setExtratoId(99L);
        transacao.setExtratoCode("EXT001");
        return transacao;
    }

    private ContaFinanceira conta() {
        ContaFinanceira conta = new ContaFinanceira(2L, 1L, "USR001", TipoConta.corrente, 10L, "BCO001",
                new BigDecimal("1000.00"), new BigDecimal("850.00"), new BigDecimal("0.00"),
                false, true, OffsetDateTime.now(), null, "N", null);
        conta.setCode("CTA001");
        return conta;
    }

    private Extrato extratoComPendentes(int pendentes) {
        Extrato extrato = new Extrato(1L, "USR001", 2L, "CTA001", "extrato.pdf", "uuid", "hash");
        extrato.setId(99L);
        extrato.setTotalLancamentos(pendentes);
        extrato.setLancamentosPendentes(pendentes);
        return extrato;
    }

    @Test
    void executar_deveConfirmarComoGasto_semMexerNoSaldo_quandoDirecaoNaoMuda() {
        when(transacaoRepository.buscarPorIdECode(10L, "TRX001"))
                .thenReturn(Optional.of(lancamentoPendente(new BigDecimal("-150.50"))));
        when(transacaoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(extratoRepository.buscarPorId(99L)).thenReturn(Optional.of(extratoComPendentes(1)));

        Transacao revisada = useCase.executar(10L, "TRX001",
                DestinoRevisaoLancamento.GASTO, null, null);

        assertThat(revisada.getStatusRevisao()).isEqualTo(StatusRevisaoTransacao.CONFIRMADA);
        assertThat(revisada.tipoEfetivo()).isEqualTo(TipoTransacao.GASTO);
        verify(contaRepository, never()).salvar(any());
    }

    @Test
    void executar_deveInverterSinalERecalcularSaldo_quandoUsuarioTrocaGastoPorReceita() {
        when(transacaoRepository.buscarPorIdECode(10L, "TRX001"))
                .thenReturn(Optional.of(lancamentoPendente(new BigDecimal("-150.50"))));
        when(transacaoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(contaRepository.buscarPorId(2L)).thenReturn(Optional.of(conta()));
        when(contaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(extratoRepository.buscarPorId(99L)).thenReturn(Optional.of(extratoComPendentes(1)));

        Transacao revisada = useCase.executar(10L, "TRX001",
                DestinoRevisaoLancamento.RECEITA, null, null);

        assertThat(revisada.tipoEfetivo()).isEqualTo(TipoTransacao.RECEITA);
        assertThat(revisada.getValor()).isEqualByComparingTo("150.50");

        ArgumentCaptor<ContaFinanceira> contaSalva = ArgumentCaptor.forClass(ContaFinanceira.class);
        verify(contaRepository).salvar(contaSalva.capture());
        // 850 desfaz o gasto (+150,50) e aplica a receita (+150,50)
        assertThat(contaSalva.getValue().getSaldoAtual()).isEqualByComparingTo("1151.00");
    }

    @Test
    void executar_deveConverterEmAporteDeEconomias_quandoDestinoEhEconomia() {
        when(transacaoRepository.buscarPorIdECode(10L, "TRX001"))
                .thenReturn(Optional.of(lancamentoPendente(new BigDecimal("-150.50"))));
        when(transacaoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(contaRepository.buscarPorId(2L)).thenReturn(Optional.of(conta()));
        when(contaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(movimentacaoEconomiaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(extratoRepository.buscarPorId(99L)).thenReturn(Optional.of(extratoComPendentes(1)));

        Transacao revisada = useCase.executar(10L, "TRX001",
                DestinoRevisaoLancamento.ECONOMIA, null, null);

        // A transação sai das listagens: IGNORADA + deletedAt preenchido.
        assertThat(revisada.getStatusRevisao()).isEqualTo(StatusRevisaoTransacao.IGNORADA);
        assertThat(revisada.getDeletedAt()).isNotNull();

        ArgumentCaptor<ContaFinanceira> contaSalva = ArgumentCaptor.forClass(ContaFinanceira.class);
        verify(contaRepository).salvar(contaSalva.capture());
        // Saldo disponível continua em 850 (o dinheiro saiu mesmo), mas agora como reserva.
        assertThat(contaSalva.getValue().getSaldoAtual()).isEqualByComparingTo("850.00");
        assertThat(contaSalva.getValue().getSaldoEconomias()).isEqualByComparingTo("150.50");

        ArgumentCaptor<MovimentacaoEconomia> movimentacao = ArgumentCaptor.forClass(MovimentacaoEconomia.class);
        verify(movimentacaoEconomiaRepository).salvar(movimentacao.capture());
        assertThat(movimentacao.getValue().getTipo()).isEqualTo(TipoMovimentacaoEconomia.APORTE);
        assertThat(movimentacao.getValue().getValor()).isEqualByComparingTo("150.50");

        ArgumentCaptor<Extrato> extratoSalvo = ArgumentCaptor.forClass(Extrato.class);
        verify(extratoRepository).salvar(extratoSalvo.capture());
        assertThat(extratoSalvo.getValue().getLancamentosIgnorados()).isEqualTo(1);
        assertThat(extratoSalvo.getValue().getLancamentosConfirmados()).isZero();
    }

    @Test
    void executar_deveTrocarParaCategoriaInformada_quandoCompativelComODestino() {
        when(transacaoRepository.buscarPorIdECode(10L, "TRX001"))
                .thenReturn(Optional.of(lancamentoPendente(new BigDecimal("-150.50"))));
        when(transacaoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(categoriaRepository.buscarPorIdECode(3L, "CAT003")).thenReturn(Optional.of(
                new Categoria(3L, "Alimentação", TipoCategoria.GASTO, "cart", "#FF0000", true, OffsetDateTime.now())));
        when(extratoRepository.buscarPorId(99L)).thenReturn(Optional.of(extratoComPendentes(1)));

        Transacao revisada = useCase.executar(10L, "TRX001",
                DestinoRevisaoLancamento.GASTO, 3L, "CAT003");

        assertThat(revisada.getCategoriaId()).isEqualTo(3L);
        // Categoria de tipo fixo guarda o valor positivo — a direção vem da categoria.
        assertThat(revisada.getValor()).isEqualByComparingTo("150.50");
        assertThat(revisada.tipoEfetivo()).isEqualTo(TipoTransacao.GASTO);
    }

    @Test
    void executar_deveRecusarCategoriaIncompativelComODestino() {
        when(transacaoRepository.buscarPorIdECode(10L, "TRX001"))
                .thenReturn(Optional.of(lancamentoPendente(new BigDecimal("-150.50"))));
        when(categoriaRepository.buscarPorIdECode(4L, "CAT004")).thenReturn(Optional.of(
                new Categoria(4L, "Salário", TipoCategoria.RECEITA, "wallet", "#00FF00", true, OffsetDateTime.now())));

        assertThatThrownBy(() -> useCase.executar(10L, "TRX001",
                DestinoRevisaoLancamento.GASTO, 4L, "CAT004"))
                .isInstanceOf(TransacaoInvalidaException.class)
                .hasMessageContaining("não é compatível");

        verify(transacaoRepository, never()).salvar(any());
    }

    @Test
    void executar_deveCairNaCategoriaGenerica_quandoDirecaoMudaSemCategoriaInformada() {
        Transacao lancamento = lancamentoPendente(new BigDecimal("150.50"));
        lancamento.setCategoriaId(3L);
        lancamento.setCategoriaCode("CAT003");
        lancamento.setCategoriaTipo(TipoCategoria.GASTO);

        when(transacaoRepository.buscarPorIdECode(10L, "TRX001")).thenReturn(Optional.of(lancamento));
        when(transacaoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(categoriaRepository.listarPorTipo(TipoCategoria.AMBOS)).thenReturn(List.of(
                new Categoria(7L, "Outros", TipoCategoria.AMBOS, "more", "#7F8C8D", true, OffsetDateTime.now())));
        when(contaRepository.buscarPorId(2L)).thenReturn(Optional.of(conta()));
        when(contaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(extratoRepository.buscarPorId(99L)).thenReturn(Optional.of(extratoComPendentes(1)));

        Transacao revisada = useCase.executar(10L, "TRX001",
                DestinoRevisaoLancamento.RECEITA, null, null);

        assertThat(revisada.getCategoriaId()).isEqualTo(7L);
        assertThat(revisada.tipoEfetivo()).isEqualTo(TipoTransacao.RECEITA);
    }

    @Test
    void executar_semDestino_deveManterAClassificacaoDoExtrato() {
        when(transacaoRepository.buscarPorIdECode(10L, "TRX001"))
                .thenReturn(Optional.of(lancamentoPendente(new BigDecimal("-150.50"))));
        when(transacaoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(extratoRepository.buscarPorId(99L)).thenReturn(Optional.of(extratoComPendentes(2)));

        Transacao revisada = useCase.executar(10L, "TRX001");

        assertThat(revisada.getStatusRevisao()).isEqualTo(StatusRevisaoTransacao.CONFIRMADA);
        assertThat(revisada.tipoEfetivo()).isEqualTo(TipoTransacao.GASTO);
        verify(contaRepository, never()).buscarPorId(anyLong());
    }
}
