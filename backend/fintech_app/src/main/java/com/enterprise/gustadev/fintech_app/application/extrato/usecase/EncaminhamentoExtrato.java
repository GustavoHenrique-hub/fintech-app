package com.enterprise.gustadev.fintech_app.application.extrato.usecase;

import com.enterprise.gustadev.fintech_app.application.extrato.parser.DetectorFormatoExtrato;
import com.enterprise.gustadev.fintech_app.application.extrato.parser.ExtratoParser;
import com.enterprise.gustadev.fintech_app.application.extrato.parser.LancamentoExtraido;
import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.SolicitacaoProcessamentoExtrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ProcessamentoExtratoPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.FormatoExtrato;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusExtrato;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusRevisaoTransacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;

import java.util.List;

/**
 * Leva um extrato já salvo até quem extrai os lançamentos — compartilhado entre a
 * importação ({@link ImportarExtratoUseCase}) e o reenvio ({@link ReenviarExtratoUseCase}):
 *
 * <ul>
 *   <li><b>PDF</b> → automação N8N + IA. O extrato fica {@code na_fila} e as transações
 *       nascem depois, no callback.</li>
 *   <li><b>CSV/TXT/XLS/XLSX</b>, ou PDF com a automação fora do ar → parser local, síncrono.</li>
 * </ul>
 */
class EncaminhamentoExtrato {

    private static final String MIME_PDF = "application/pdf";
    /** Canal de entrada do extrato, na nomenclatura dos workflows do N8N. */
    private static final String ORIGEM_APP = "app";

    private final ExtratoRepositoryPort extratoRepository;
    private final ContaFinanceiraRepositoryPort contaRepository;
    private final CategoriaRepositoryPort categoriaRepository;
    private final TransacaoRepositoryPort transacaoRepository;
    private final ProcessamentoExtratoPort processamento;
    private final List<ExtratoParser> parsers;

    EncaminhamentoExtrato(ExtratoRepositoryPort extratoRepository,
                          ContaFinanceiraRepositoryPort contaRepository,
                          CategoriaRepositoryPort categoriaRepository,
                          TransacaoRepositoryPort transacaoRepository,
                          ProcessamentoExtratoPort processamento,
                          List<ExtratoParser> parsers) {
        this.extratoRepository = extratoRepository;
        this.contaRepository = contaRepository;
        this.categoriaRepository = categoriaRepository;
        this.transacaoRepository = transacaoRepository;
        this.processamento = processamento;
        this.parsers = parsers;
    }

    Extrato encaminhar(Extrato extrato, ContaFinanceira conta, byte[] conteudo) {
        FormatoExtrato formato = DetectorFormatoExtrato.detectar(extrato.getArquivoNome());

        // PDF é o único formato que a automação entende hoje; para os demais o
        // parser local continua sendo o caminho principal.
        if (formato == FormatoExtrato.PDF) {
            boolean aceito = processamento.enviarParaProcessamento(new SolicitacaoProcessamentoExtrato(
                    extrato.getId(), extrato.getCode(), extrato.getUsuarioId(), conta.getId(),
                    extrato.getArquivoNome(), MIME_PDF, conteudo, ORIGEM_APP));
            if (aceito) {
                extrato.setStatus(StatusExtrato.na_fila);
                return extratoRepository.salvar(extrato);
            }
        }

        ExtratoParser parser = parsers.stream()
                .filter(p -> p.suporta(formato))
                .findFirst()
                .orElseThrow(() -> new ExtratoInvalidoException(
                        "Nenhum parser disponível para o formato " + formato));

        List<LancamentoExtraido> lancamentos;
        try {
            lancamentos = parser.parsear(conteudo);
        } catch (ExtratoInvalidoException e) {
            extrato.setStatus(StatusExtrato.erro_extracao);
            extratoRepository.salvar(extrato);
            throw e;
        }

        Categoria categoriaFallback = CatalogoCategoriasImportacao.carregar(categoriaRepository).fallback();
        int criadas = 0;
        for (LancamentoExtraido lancamento : lancamentos) {
            if (lancamento.valor().signum() == 0) continue;

            Transacao transacao = new Transacao(conta, lancamento.valor(), lancamento.data(),
                    categoriaFallback.getId(), categoriaFallback.getCode(), OrigemTransacao.importado);
            transacao.setDescricao(lancamento.descricao());
            transacao.setCategoriaTipo(categoriaFallback.getTipo());
            transacao.setStatusRevisao(StatusRevisaoTransacao.PENDENTE_REVISAO);
            transacao.setExtratoId(extrato.getId());
            transacao.setExtratoCode(extrato.getCode());
            transacao.validar();

            transacaoRepository.salvar(transacao);
            conta.aplicarTransacao(transacao.tipoEfetivo(), transacao.getValor().abs());
            criadas++;
        }
        contaRepository.salvar(conta);

        extrato.setTotalLancamentos(criadas);
        extrato.setLancamentosPendentes(criadas);
        extrato.setStatus(criadas > 0 ? StatusExtrato.pendente_revisao : StatusExtrato.erro_extracao);
        return extratoRepository.salvar(extrato);
    }
}
