package com.enterprise.gustadev.fintech_app.application.transacaocancelada.usecase;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.motivocancelamento.exception.MotivoCancelamentoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.motivocancelamento.model.MotivoCancelamento;
import com.enterprise.gustadev.fintech_app.domain.motivocancelamento.port.MotivoCancelamentoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoNaoEncontradaException;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.transacaocancelada.model.TransacaoCancelada;
import com.enterprise.gustadev.fintech_app.domain.transacaocancelada.port.TransacaoCanceladaRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

public class CancelarTransacaoUseCase {

    private final TransacaoCanceladaRepositoryPort repository;
    private final TransacaoRepositoryPort transacaoRepository;
    private final ContaFinanceiraRepositoryPort contaRepository;
    private final MotivoCancelamentoRepositoryPort motivoRepository;

    public CancelarTransacaoUseCase(TransacaoCanceladaRepositoryPort repository,
                                     TransacaoRepositoryPort transacaoRepository,
                                     ContaFinanceiraRepositoryPort contaRepository,
                                     MotivoCancelamentoRepositoryPort motivoRepository) {
        this.repository = repository;
        this.transacaoRepository = transacaoRepository;
        this.contaRepository = contaRepository;
        this.motivoRepository = motivoRepository;
    }

    @Transactional
    public TransacaoCancelada executar(TransacaoCancelada transacaoCancelada) {
        transacaoCancelada.validar();

        return repository.buscarPorTransacao(transacaoCancelada.getTransacaoId())
                .orElseGet(() -> {
                    Transacao transacao = transacaoRepository
                            .buscarPorId(transacaoCancelada.getTransacaoId())
                            .orElseThrow(() -> new TransacaoNaoEncontradaException(
                                    "Transação não encontrada: " + transacaoCancelada.getTransacaoId()));

                    MotivoCancelamento motivo = motivoRepository
                            .buscarPorId(transacaoCancelada.getMotivoId())
                            .orElseThrow(() -> new MotivoCancelamentoInvalidoException(
                                    "Motivo de cancelamento não encontrado: " + transacaoCancelada.getMotivoId()));

                    ContaFinanceira conta = contaRepository
                            .buscarPorId(transacao.getConta().getId())
                            .orElseThrow(() -> new ContaFinanceiraInvalidaException(
                                    "Conta financeira não encontrada: " + transacao.getConta().getId()));

                    conta.reverterTransacao(transacao.tipoEfetivo(), transacao.getValor().abs());
                    contaRepository.salvar(conta);

                    transacao.arquivar();
                    transacaoRepository.salvar(transacao);

                    transacaoCancelada.setTransacaoCode(transacao.getCode());
                    transacaoCancelada.setMotivoCode(motivo.getCode());

                    return repository.salvar(transacaoCancelada);
                });
    }
}
