package com.enterprise.gustadev.fintech_app.application.transacaocancelada.usecase;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
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

    public CancelarTransacaoUseCase(TransacaoCanceladaRepositoryPort repository,
                                     TransacaoRepositoryPort transacaoRepository,
                                     ContaFinanceiraRepositoryPort contaRepository) {
        this.repository = repository;
        this.transacaoRepository = transacaoRepository;
        this.contaRepository = contaRepository;
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

                    ContaFinanceira conta = contaRepository
                            .buscarPorId(transacao.getConta().getId())
                            .orElseThrow(() -> new ContaFinanceiraInvalidaException(
                                    "Conta financeira não encontrada: " + transacao.getConta().getId()));

                    conta.reverterTransacao(transacao.getTipo(), transacao.getValor());
                    contaRepository.salvar(conta);

                    transacao.arquivar();
                    transacaoRepository.salvar(transacao);

                    return repository.salvar(transacaoCancelada);
                });
    }
}
