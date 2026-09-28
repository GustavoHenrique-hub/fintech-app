package com.enterprise.gustadev.fintech_app.application.extrato;

import com.enterprise.gustadev.fintech_app.application.classificacao.CalibradorConfiancaIa;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.RegistrarResultadoExtratoUseCase;
import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.classificacao.model.AprendizadoClassificacao;
import com.enterprise.gustadev.fintech_app.domain.classificacao.port.AprendizadoClassificacaoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.ResultadoProcessamentoExtrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusExtrato;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusRevisaoTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarResultadoExtratoUseCaseTest {

    @Mock
    private ExtratoRepositoryPort extratoRepository;
    @Mock
    private ContaFinanceiraRepositoryPort contaRepository;
    @Mock
    private CategoriaRepositoryPort categoriaRepository;
    @Mock
    private TransacaoRepositoryPort transacaoRepository;
    @Mock
    private AprendizadoClassificacaoRepositoryPort aprendizadoRepository;

    private RegistrarResultadoExtratoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegistrarResultadoExtratoUseCase(extratoRepository, contaRepository,
                categoriaRepository, transacaoRepository, new CalibradorConfiancaIa(aprendizadoRepository));
    }

    private Extrato extratoNaFila() {
        Extrato extrato = new Extrato(1L, "USR001", 2L, "CTA001", "extrato.pdf", "uuid", "hash");
        extrato.setId(42L);
        extrato.setStatus(StatusExtrato.na_fila);
        return extrato;
    }

    private ContaFinanceira conta() {
        return new ContaFinanceira(2L, 1L, "USR001", TipoConta.corrente, 10L, "BCO001",
                new BigDecimal("1000.00"), new BigDecimal("1000.00"), new BigDecimal("0.00"),
                false, true, OffsetDateTime.now(), null, "N", null);
    }

    private Categoria outros() {
        return new Categoria(7L, "Outros", TipoCategoria.AMBOS, "more", "#7F8C8D", true, OffsetDateTime.now());
    }

    private ResultadoProcessamentoExtrato resultadoComDoisLancamentos() {
        return new ResultadoProcessamentoExtrato("pendente_revisao", "Nubank",
                LocalDate.parse("2026-08-01"), LocalDate.parse("2026-08-31"),
                List.of(
                        new ResultadoProcessamentoExtrato.LancamentoProcessado(
                                LocalDate.parse("2026-08-05"), "SUPERMERCADO XYZ", "Supermercado XYZ",
                                new BigDecimal("150.50"), TipoTransacao.GASTO, "alimentacao", (short) 92, null),
                        new ResultadoProcessamentoExtrato.LancamentoProcessado(
                                LocalDate.parse("2026-08-06"), "SALARIO", null,
                                new BigDecimal("5000.00"), TipoTransacao.RECEITA, "salario", (short) 98, null)));
    }

    @Test
    void executar_deveDeixarPendenteAbaixoDe95_eConfirmarSozinhoAPartirDe95() {
        when(extratoRepository.buscarPorId(42L)).thenReturn(Optional.of(extratoNaFila()));
        when(extratoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(contaRepository.buscarPorId(2L)).thenReturn(Optional.of(conta()));
        when(contaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(categoriaRepository.listarPorTipo(any())).thenReturn(List.of(outros()));
        when(transacaoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        Extrato resultado = useCase.executar(42L, resultadoComDoisLancamentos());

        // SALARIO veio com 98% de confiança: já nasce confirmado.
        assertThat(resultado.getStatus()).isEqualTo(StatusExtrato.parcialmente_revisado);
        assertThat(resultado.getTotalLancamentos()).isEqualTo(2);
        assertThat(resultado.getLancamentosPendentes()).isEqualTo(1);
        assertThat(resultado.getLancamentosConfirmados()).isEqualTo(1);
        assertThat(resultado.getBancoDetectado()).isEqualTo("Nubank");
        assertThat(resultado.getPeriodoInicio()).isEqualTo(LocalDate.parse("2026-08-01"));

        ArgumentCaptor<Transacao> transacoes = ArgumentCaptor.forClass(Transacao.class);
        verify(transacaoRepository, times(2)).salvar(transacoes.capture());
        Transacao gasto = transacoes.getAllValues().get(0);
        assertThat(gasto.getStatusRevisao()).isEqualTo(StatusRevisaoTransacao.PENDENTE_REVISAO);
        assertThat(gasto.getConfiancaIa()).isEqualTo((short) 92);
        assertThat(gasto.getExtratoId()).isEqualTo(42L);
        // Sem categoria "alimentacao" cadastrada, cai na genérica e o sinal carrega a direção.
        assertThat(gasto.getCategoriaId()).isEqualTo(7L);
        assertThat(gasto.getValor()).isEqualByComparingTo("-150.50");
        assertThat(gasto.tipoEfetivo()).isEqualTo(TipoTransacao.GASTO);
        assertThat(gasto.saldoJaAplicado()).isFalse();

        Transacao salario = transacoes.getAllValues().get(1);
        assertThat(salario.getStatusRevisao()).isEqualTo(StatusRevisaoTransacao.CONFIRMADA);
        assertThat(salario.saldoJaAplicado()).isTrue();

        // Só o confirmado entra no saldo; o gasto pendente espera a revisão.
        ArgumentCaptor<ContaFinanceira> contaSalva = ArgumentCaptor.forClass(ContaFinanceira.class);
        verify(contaRepository).salvar(contaSalva.capture());
        assertThat(contaSalva.getValue().getSaldoAtual()).isEqualByComparingTo("6000.00");
    }

    @Test
    void executar_deveConfirmarSozinho_quandoAprendizadoElevaAConfiancaAcimaDe95() {
        AprendizadoClassificacao aprendizado = new AprendizadoClassificacao(1L, "supermercado xyz", TipoTransacao.GASTO);
        aprendizado.registrarAcerto(TipoTransacao.GASTO, null, null);
        aprendizado.registrarAcerto(TipoTransacao.GASTO, null, null);  // +8 pontos

        when(extratoRepository.buscarPorId(42L)).thenReturn(Optional.of(extratoNaFila()));
        when(extratoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(contaRepository.buscarPorId(2L)).thenReturn(Optional.of(conta()));
        when(contaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(categoriaRepository.listarPorTipo(any())).thenReturn(List.of(outros()));
        when(transacaoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(aprendizadoRepository.buscarPorUsuarioEChave(1L, "supermercado xyz")).thenReturn(Optional.of(aprendizado));

        Extrato resultado = useCase.executar(42L, new ResultadoProcessamentoExtrato(
                "pendente_revisao", "Nubank", null, null,
                List.of(new ResultadoProcessamentoExtrato.LancamentoProcessado(
                        LocalDate.parse("2026-08-05"), "SUPERMERCADO XYZ", null,
                        new BigDecimal("150.50"), TipoTransacao.GASTO, null, (short) 90, null))));

        ArgumentCaptor<Transacao> transacao = ArgumentCaptor.forClass(Transacao.class);
        verify(transacaoRepository).salvar(transacao.capture());
        assertThat(transacao.getValue().getConfiancaIa()).isEqualTo((short) 98);
        assertThat(transacao.getValue().getStatusRevisao()).isEqualTo(StatusRevisaoTransacao.CONFIRMADA);
        assertThat(resultado.getStatus()).isEqualTo(StatusExtrato.concluido);
    }

    @Test
    void executar_naoDeveConfirmarSozinho_quandoUsuarioJaClassificouADescricaoNaOutraDirecao() {
        AprendizadoClassificacao aprendizado = new AprendizadoClassificacao(1L, "salario", TipoTransacao.GASTO);
        aprendizado.registrarAcerto(TipoTransacao.GASTO, null, null);

        when(extratoRepository.buscarPorId(42L)).thenReturn(Optional.of(extratoNaFila()));
        when(extratoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(contaRepository.buscarPorId(2L)).thenReturn(Optional.of(conta()));
        when(categoriaRepository.listarPorTipo(any())).thenReturn(List.of(outros()));
        when(transacaoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(aprendizadoRepository.buscarPorUsuarioEChave(1L, "salario")).thenReturn(Optional.of(aprendizado));

        useCase.executar(42L, new ResultadoProcessamentoExtrato(
                "pendente_revisao", "Nubank", null, null,
                List.of(new ResultadoProcessamentoExtrato.LancamentoProcessado(
                        LocalDate.parse("2026-08-06"), "SALARIO", null,
                        new BigDecimal("5000.00"), TipoTransacao.RECEITA, null, (short) 99, null))));

        ArgumentCaptor<Transacao> transacao = ArgumentCaptor.forClass(Transacao.class);
        verify(transacaoRepository).salvar(transacao.capture());
        assertThat(transacao.getValue().getConfiancaIa()).isEqualTo((short) 69);
        assertThat(transacao.getValue().getStatusRevisao()).isEqualTo(StatusRevisaoTransacao.PENDENTE_REVISAO);
        verify(contaRepository, never()).salvar(any());
    }

    @Test
    void executar_deveUsarACategoriaSugeridaPelaIa_quandoExisteCadastrada() {
        Categoria alimentacao = new Categoria(3L, "Alimentação", TipoCategoria.GASTO,
                "cart", "#FF0000", true, OffsetDateTime.now());

        when(extratoRepository.buscarPorId(42L)).thenReturn(Optional.of(extratoNaFila()));
        when(extratoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(contaRepository.buscarPorId(2L)).thenReturn(Optional.of(conta()));
        when(categoriaRepository.listarPorTipo(TipoCategoria.AMBOS)).thenReturn(List.of(outros()));
        when(categoriaRepository.listarPorTipo(TipoCategoria.GASTO)).thenReturn(List.of(alimentacao));
        when(transacaoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        ResultadoProcessamentoExtrato resultado = new ResultadoProcessamentoExtrato(
                "pendente_revisao", "Nubank", null, null,
                List.of(new ResultadoProcessamentoExtrato.LancamentoProcessado(
                        LocalDate.parse("2026-08-05"), "SUPERMERCADO XYZ", null,
                        new BigDecimal("150.50"), TipoTransacao.GASTO, "alimentacao", (short) 92, null)));

        useCase.executar(42L, resultado);

        ArgumentCaptor<Transacao> transacao = ArgumentCaptor.forClass(Transacao.class);
        verify(transacaoRepository).salvar(transacao.capture());
        assertThat(transacao.getValue().getCategoriaId()).isEqualTo(3L);
        // Categoria de tipo fixo guarda valor positivo.
        assertThat(transacao.getValue().getValor()).isEqualByComparingTo("150.50");
        assertThat(transacao.getValue().tipoEfetivo()).isEqualTo(TipoTransacao.GASTO);
    }

    @Test
    void executar_deveRegistrarStatusDeErro_semCriarTransacoes() {
        when(extratoRepository.buscarPorId(42L)).thenReturn(Optional.of(extratoNaFila()));
        when(extratoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        Extrato resultado = useCase.executar(42L, new ResultadoProcessamentoExtrato(
                "erro_extracao", null, null, null, List.of()));

        assertThat(resultado.getStatus()).isEqualTo(StatusExtrato.erro_extracao);
        verify(transacaoRepository, never()).salvar(any());
        verify(contaRepository, never()).salvar(any());
    }

    @Test
    void executar_deveIgnorarCallbackRepetido_quandoExtratoJaSaiuDoProcessamento() {
        Extrato jaRevisado = extratoNaFila();
        jaRevisado.setStatus(StatusExtrato.pendente_revisao);
        jaRevisado.setTotalLancamentos(2);
        when(extratoRepository.buscarPorId(42L)).thenReturn(Optional.of(jaRevisado));

        Extrato resultado = useCase.executar(42L, resultadoComDoisLancamentos());

        assertThat(resultado.getTotalLancamentos()).isEqualTo(2);
        verify(transacaoRepository, never()).salvar(any());
        verify(extratoRepository, never()).salvar(any());
    }
}
