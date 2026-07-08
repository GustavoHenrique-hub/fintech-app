package com.enterprise.gustadev.fintech_app.application.extrato.usecase;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;

public class CriarExtratoUseCase {

    private final ExtratoRepositoryPort repository;
    private final ContaFinanceiraRepositoryPort contaRepository;

    public CriarExtratoUseCase(ExtratoRepositoryPort repository,
                                ContaFinanceiraRepositoryPort contaRepository) {
        this.repository = repository;
        this.contaRepository = contaRepository;
    }

    public Extrato executar(Long usuarioId, Long contaId, String arquivoNome,
                            String arquivoUuid, String hashArquivo) {
        ContaFinanceira conta = contaRepository.buscarPorId(contaId)
                .orElseThrow(() -> new ContaFinanceiraInvalidaException(
                        "Conta financeira não encontrada: id=" + contaId));

        if (!conta.getUsuarioId().equals(usuarioId)) {
            throw new ExtratoInvalidoException(
                    "Conta financeira não pertence ao usuário informado");
        }

        Extrato extrato = new Extrato(
                conta.getUsuarioId(), conta.getUsuarioCode(),
                conta.getId(), conta.getCode(),
                arquivoNome, arquivoUuid, hashArquivo);
        extrato.validar();

        repository.buscarPorHash(extrato.getHashArquivo()).ifPresent(e -> {
            throw new ExtratoInvalidoException("Extrato duplicado: hash já processado");
        });
        return repository.salvar(extrato);
    }
}
