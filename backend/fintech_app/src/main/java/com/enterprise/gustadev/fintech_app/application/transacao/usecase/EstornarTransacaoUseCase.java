package com.enterprise.gustadev.fintech_app.application.transacao.usecase;

import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoNaoEncontradaException;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

public class EstornarTransacaoUseCase {

    private final TransacaoRepositoryPort repository;

    public EstornarTransacaoUseCase(TransacaoRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional
    public Transacao executar(Long id, String code, Long usuarioId, String usuarioCode,
                              Long contaId, String contaCode) {
        Transacao original = repository
                .buscarTransacao(id, code, usuarioId, usuarioCode, contaId, contaCode)
                .orElseThrow(() -> new TransacaoNaoEncontradaException(
                        "Transação não encontrada para estorno: id=" + id + ", code=" + code));

        // Idempotência: se já existe um estorno para esta transação, retorna o existente
        // em vez de inserir um duplicado.
        return repository.buscarEstornoDe(original.getId())
                .orElseGet(() -> repository.salvar(original.criarEstorno()));
    }
}
