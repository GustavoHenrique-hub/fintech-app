package com.enterprise.gustadev.fintech_app.application.transacao.usecase;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoNaoEncontradaException;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

public class EstornarTransacaoUseCase {

    private final TransacaoRepositoryPort repository;
    private final ContaFinanceiraRepositoryPort contaRepository;

    public EstornarTransacaoUseCase(TransacaoRepositoryPort repository,
                                     ContaFinanceiraRepositoryPort contaRepository) {
        this.repository = repository;
        this.contaRepository = contaRepository;
    }

    @Transactional
    public Transacao executar(Long id, String code, Long contaId, String contaCode) {
        Transacao original = repository
                .buscarTransacao(id, code, contaId, contaCode)
                .orElseThrow(() -> new TransacaoNaoEncontradaException(
                        "Transação não encontrada para estorno: id=" + id + ", code=" + code));

        return repository.buscarEstornoDe(original.getId())
                .orElseGet(() -> {
                    Transacao estorno = original.criarEstorno();
                    repository.salvar(original);
                    Transacao estornoSalvo = repository.salvar(estorno);

                    ContaFinanceira conta = contaRepository.buscarPorId(original.getConta().getId())
                            .orElseThrow(() -> new ContaFinanceiraInvalidaException(
                                    "Conta financeira não encontrada: " + original.getConta().getId()));
                    conta.reverterTransacao(original.tipoEfetivo(), original.getValor().abs());
                    contaRepository.salvar(conta);

                    return estornoSalvo;
                });
    }
}
