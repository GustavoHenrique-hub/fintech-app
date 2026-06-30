package com.enterprise.gustadev.fintech_app.application.transacao.usecase;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

public class CriarTransacaoUseCase {

    private final TransacaoRepositoryPort repository;
    private final ContaFinanceiraRepositoryPort contaRepository;

    public CriarTransacaoUseCase(TransacaoRepositoryPort repository,
                                  ContaFinanceiraRepositoryPort contaRepository) {
        this.repository = repository;
        this.contaRepository = contaRepository;
    }

    @Transactional
    public Transacao executar(Transacao transacao) {
        transacao.validar();
        Transacao salva = repository.salvar(transacao);

        ContaFinanceira conta = contaRepository.buscarPorId(transacao.getConta().getId())
                .orElseThrow(() -> new ContaFinanceiraInvalidaException(
                        "Conta financeira não encontrada: " + transacao.getConta().getId()));
        conta.aplicarTransacao(transacao.getTipo(), transacao.getValor());
        contaRepository.salvar(conta);

        return salva;
    }
}
