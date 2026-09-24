package com.enterprise.gustadev.fintech_app.application.extrato.usecase;

import com.enterprise.gustadev.fintech_app.application.extrato.parser.ExtratoParser;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ArmazenamentoArquivoPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ProcessamentoExtratoPort;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Tenta de novo um extrato cujo processamento terminou em erro (ou foi cancelado),
 * usando o arquivo que ficou armazenado no upload. Existe porque o upload recusa o
 * mesmo arquivo pelo hash: sem isso, um extrato com erro não teria como ser lido.
 *
 * <p>O extrato mantém id/code e passa pelo mesmo encaminhamento da importação
 * (PDF → N8N + IA; demais formatos → parser local).
 */
public class ReenviarExtratoUseCase {

    private final ExtratoRepositoryPort extratoRepository;
    private final ContaFinanceiraRepositoryPort contaRepository;
    private final ArmazenamentoArquivoPort armazenamento;
    private final EncaminhamentoExtrato encaminhamento;

    public ReenviarExtratoUseCase(ExtratoRepositoryPort extratoRepository,
                                  ContaFinanceiraRepositoryPort contaRepository,
                                  CategoriaRepositoryPort categoriaRepository,
                                  TransacaoRepositoryPort transacaoRepository,
                                  ArmazenamentoArquivoPort armazenamento,
                                  ProcessamentoExtratoPort processamento,
                                  List<ExtratoParser> parsers) {
        this.extratoRepository = extratoRepository;
        this.contaRepository = contaRepository;
        this.armazenamento = armazenamento;
        this.encaminhamento = new EncaminhamentoExtrato(extratoRepository, contaRepository,
                categoriaRepository, transacaoRepository, processamento, parsers);
    }

    @Transactional
    public Extrato executar(Long id, String code) {
        Extrato extrato = extratoRepository.buscarPorIdECode(id, code)
                .orElseThrow(() -> new ExtratoInvalidoException(
                        "Extrato não encontrado: id=" + id + ", code=" + code));

        extrato.prepararReenvio();

        ContaFinanceira conta = contaRepository.buscarPorId(extrato.getContaId())
                .orElseThrow(() -> new ContaFinanceiraInvalidaException(
                        "Conta financeira do extrato não encontrada: " + extrato.getContaId()));
        byte[] conteudo = armazenamento.carregar(extrato.getArquivoUuid(), extrato.getArquivoNome());

        return encaminhamento.encaminhar(extratoRepository.salvar(extrato), conta, conteudo);
    }
}
