package com.enterprise.gustadev.fintech_app.application.extrato;

import com.enterprise.gustadev.fintech_app.application.extrato.parser.CsvExtratoParser;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.ReenviarExtratoUseCase;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.SolicitacaoProcessamentoExtrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ArmazenamentoArquivoPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ProcessamentoExtratoPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusExtrato;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReenviarExtratoUseCaseTest {

    private static final byte[] PDF = "conteudo-pdf".getBytes(StandardCharsets.UTF_8);

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

    private ReenviarExtratoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ReenviarExtratoUseCase(extratoRepository, contaRepository, categoriaRepository,
                transacaoRepository, armazenamento, processamento, List.of(new CsvExtratoParser()));
    }

    private ContaFinanceira contaValida() {
        ContaFinanceira conta = new ContaFinanceira(1L, "USR001", TipoConta.corrente,
                1L, "BCO001", new BigDecimal("1000.00"), false);
        conta.setId(1L);
        conta.setCode("CTA001");
        return conta;
    }

    private Extrato extratoEm(StatusExtrato status) {
        Extrato extrato = new Extrato(1L, "USR001", 1L, "CTA001", "extrato.pdf", "uuid-123", "hash");
        extrato.setId(11L);
        extrato.setStatus(status);
        return extrato;
    }

    @Test
    void executar_deveReenviarPdfComErroParaAutomacao_usandoOArquivoArmazenado() {
        Extrato extrato = extratoEm(StatusExtrato.erro_classificacao);
        when(extratoRepository.buscarPorIdECode(11L, extrato.getCode())).thenReturn(Optional.of(extrato));
        when(contaRepository.buscarPorId(1L)).thenReturn(Optional.of(contaValida()));
        when(armazenamento.carregar("uuid-123", "extrato.pdf")).thenReturn(PDF);
        when(extratoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(processamento.enviarParaProcessamento(any())).thenReturn(true);

        Extrato resultado = useCase.executar(11L, extrato.getCode());

        assertThat(resultado.getId()).isEqualTo(11L);
        assertThat(resultado.getStatus()).isEqualTo(StatusExtrato.na_fila);
        ArgumentCaptor<SolicitacaoProcessamentoExtrato> solicitacao =
                ArgumentCaptor.forClass(SolicitacaoProcessamentoExtrato.class);
        verify(processamento).enviarParaProcessamento(solicitacao.capture());
        assertThat(solicitacao.getValue().extratoId()).isEqualTo(11L);
        assertThat(solicitacao.getValue().conteudo()).isEqualTo(PDF);
    }

    @Test
    void executar_deveRecusar_quandoExtratoNaoTerminouEmErro() {
        Extrato extrato = extratoEm(StatusExtrato.pendente_revisao);
        when(extratoRepository.buscarPorIdECode(11L, extrato.getCode())).thenReturn(Optional.of(extrato));

        assertThatThrownBy(() -> useCase.executar(11L, extrato.getCode()))
                .isInstanceOf(ExtratoInvalidoException.class)
                .hasMessageContaining("reenviar");
        verify(processamento, never()).enviarParaProcessamento(any());
        verify(extratoRepository, never()).salvar(any());
    }

    @Test
    void executar_deveLancarExcecao_quandoExtratoNaoExiste() {
        when(extratoRepository.buscarPorIdECode(99L, "XXXXXX")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L, "XXXXXX"))
                .isInstanceOf(ExtratoInvalidoException.class)
                .hasMessageContaining("não encontrado");
    }
}
