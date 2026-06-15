package com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

public class RemoverContaFinanceiraUseCase {

    private final ContaFinanceiraRepositoryPort repository;

    public RemoverContaFinanceiraUseCase(ContaFinanceiraRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional
    public ContaFinanceira executar(Long id, String code) {
        ContaFinanceira conta = repository.buscarPorIdECode(id, code)
                .orElseThrow(() -> new ContaFinanceiraInvalidaException(
                        "Conta financeira não encontrada: id=" + id + ", code=" + code));
        conta.remover();
        return repository.salvar(conta);
    }
}
