package com.enterprise.gustadev.fintech_app.application.extrato;

import com.enterprise.gustadev.fintech_app.application.extrato.parser.CsvExtratoParser;
import com.enterprise.gustadev.fintech_app.application.extrato.parser.ExtratoParser;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.ImportarExtratoUseCase;
import com.enterprise.gustadev.fintech_app.domain.categoria.exception.CategoriaInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.SolicitacaoProcessamentoExtrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ArmazenamentoArquivoPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ProcessamentoExtratoPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusExtrato;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportarExtratoUseCaseTest {

    private static final byte[] CSV_VALIDO = (
            "05/08/2026;Supermercado;-150,50\n" +
            "06/08/2026;Salario;5000,00\n"
    ).getBytes(StandardCharsets.UTF_8);

    @Mock
    private ExtratoRepositoryPort extratoRepository;
    @Mock
    private ContaFinanceiraRepositoryPort contaRepository;
    @Mock
    private CategoriaRepositoryPort categoriaRepository;
    @Mock
    private TransacaoRepositoryPort transacaoRepository;
    @Mock
    private ArmazenamentoArquivoPort armazenamento;
    @Mock
    private ProcessamentoExtratoPort processamento;

    private ImportarExtratoUseCase useCase;

    @BeforeEach
    void setUp() {
        List<ExtratoParser> parsers = List.of(new CsvExtratoParser());
        useCase = new ImportarExtratoUseCase(extratoRepository, contaRepository, categoriaRepository,
                transacaoRepository, armazenamento, processamento, parsers);
    }

    private ContaFinanceira contaValida() {
        ContaFinanceira conta = new ContaFinanceira(1L, "USR001", TipoConta.corrente,
                1L, "BCO001", new BigDecimal("1000.00"), false);
        conta.setId(1L);
        conta.setCode("CTA001");
        return conta;
    }

    private Categoria categoriaOutros() {
        return new Categoria(7L, "Outros", TipoCategoria.AMBOS, "more", "#7F8C8D", true, OffsetDateTime.now());
    }

    @Test
    void executar_deveImportarLancamentosECriarTransacoesPendentesDeRevisao() {
        when(contaRepository.buscarPorId(1L)).thenReturn(Optional.of(contaValida()));
        when(extratoRepository.buscarPorHash(anyString())).thenReturn(Optional.empty());
        when(extratoRepository.salvar(any())).thenAnswer(inv -> {
            Extrato e = inv.getArgument(0);
            if (e.getId() == null) e.setId(10L);
            return e;
        });
        when(categoriaRepository.listarPorTipo(TipoCategoria.AMBOS)).thenReturn(List.of(categoriaOutros()));
        when(transacaoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(contaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        Extrato resultado = useCase.executar(1L, 1L, "extrato.csv", CSV_VALIDO);

        assertThat(resultado.getStatus()).isEqualTo(StatusExtrato.pendente_revisao);
        assertThat(resultado.getTotalLancamentos()).isEqualTo(2);
        assertThat(resultado.getLancamentosPendentes()).isEqualTo(2);
        verify(transacaoRepository, times(2)).salvar(any());
        verify(armazenamento).salvar(anyString(), eq("extrato.csv"), any());
        verify(contaRepository).salvar(any());
    }

    @Test
    void executar_deveEncaminharPdfParaAutomacao_semLerOArquivoLocalmente() {
        when(contaRepository.buscarPorId(1L)).thenReturn(Optional.of(contaValida()));
        when(extratoRepository.buscarPorHash(anyString())).thenReturn(Optional.empty());
        when(extratoRepository.salvar(any())).thenAnswer(inv -> {
            Extrato e = inv.getArgument(0);
            if (e.getId() == null) e.setId(10L);
            return e;
        });
        when(processamento.enviarParaProcessamento(any())).thenReturn(true);

        Extrato resultado = useCase.executar(1L, 1L, "extrato.pdf", "conteudo-pdf".getBytes(StandardCharsets.UTF_8));

        // As transações só nascem no callback da automação.
        assertThat(resultado.getStatus()).isEqualTo(StatusExtrato.na_fila);
        assertThat(resultado.getTotalLancamentos()).isZero();
        verify(transacaoRepository, never()).salvar(any());
        verify(armazenamento).salvar(anyString(), eq("extrato.pdf"), any());

        ArgumentCaptor<SolicitacaoProcessamentoExtrato> solicitacao =
                ArgumentCaptor.forClass(SolicitacaoProcessamentoExtrato.class);
        verify(processamento).enviarParaProcessamento(solicitacao.capture());
        assertThat(solicitacao.getValue().extratoId()).isEqualTo(10L);
        assertThat(solicitacao.getValue().mimeType()).isEqualTo("application/pdf");
        assertThat(solicitacao.getValue().origem()).isEqualTo("app");
    }

    @Test
    void executar_naoDeveChamarAutomacao_paraFormatosQueOParserLocalJaResolve() {
        when(contaRepository.buscarPorId(1L)).thenReturn(Optional.of(contaValida()));
        when(extratoRepository.buscarPorHash(anyString())).thenReturn(Optional.empty());
        when(extratoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(categoriaRepository.listarPorTipo(TipoCategoria.AMBOS)).thenReturn(List.of(categoriaOutros()));
        when(transacaoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(contaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        useCase.executar(1L, 1L, "extrato.csv", CSV_VALIDO);

        verify(processamento, never()).enviarParaProcessamento(any());
    }

    @Test
    void executar_deveLancarExcecao_quandoHashJaProcessado() {
        when(contaRepository.buscarPorId(1L)).thenReturn(Optional.of(contaValida()));
        when(extratoRepository.buscarPorHash(anyString()))
                .thenReturn(Optional.of(new Extrato(1L, "USR001", 1L, "CTA001", "old.csv", "uuid", "hash")));

        assertThatThrownBy(() -> useCase.executar(1L, 1L, "extrato.csv", CSV_VALIDO))
                .isInstanceOf(ExtratoInvalidoException.class)
                .hasMessageContaining("duplicado");

        verify(armazenamento, never()).salvar(anyString(), anyString(), any());
        verify(extratoRepository, never()).salvar(any());
    }

    @Test
    void executar_deveLancarExcecao_quandoFormatoNaoSuportado() {
        when(contaRepository.buscarPorId(1L)).thenReturn(Optional.of(contaValida()));

        assertThatThrownBy(() -> useCase.executar(1L, 1L, "extrato.docx", CSV_VALIDO))
                .isInstanceOf(ExtratoInvalidoException.class)
                .hasMessageContaining("não suportado");

        verify(armazenamento, never()).salvar(anyString(), anyString(), any());
    }

    @Test
    void executar_deveLancarExcecao_quandoArquivoVazio() {
        assertThatThrownBy(() -> useCase.executar(1L, 1L, "extrato.csv", new byte[0]))
                .isInstanceOf(ExtratoInvalidoException.class);

        verify(contaRepository, never()).buscarPorId(any());
    }

    @Test
    void executar_deveLancarExcecao_quandoNenhumaCategoriaAmbosCadastrada() {
        when(contaRepository.buscarPorId(1L)).thenReturn(Optional.of(contaValida()));
        when(extratoRepository.buscarPorHash(anyString())).thenReturn(Optional.empty());
        when(extratoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(categoriaRepository.listarPorTipo(TipoCategoria.AMBOS)).thenReturn(List.of());

        assertThatThrownBy(() -> useCase.executar(1L, 1L, "extrato.csv", CSV_VALIDO))
                .isInstanceOf(CategoriaInvalidaException.class);

        verify(transacaoRepository, never()).salvar(any());
    }
}
