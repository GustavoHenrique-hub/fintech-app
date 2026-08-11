package com.enterprise.gustadev.fintech_app.application.transacao.usecase;

import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoNaoEncontradaException;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

/**
 * Confirma a revisão manual de um lançamento importado de extrato (PENDENTE_REVISAO
 * → CONFIRMADA). Quando a transação veio de um extrato, também atualiza os
 * contadores agregados do extrato (lancamentosPendentes/Confirmados e status).
 */
public class ConfirmarRevisaoTransacaoUseCase {

    private final TransacaoRepositoryPort transacaoRepository;
    private final ExtratoRepositoryPort extratoRepository;

    public ConfirmarRevisaoTransacaoUseCase(TransacaoRepositoryPort transacaoRepository,
                                             ExtratoRepositoryPort extratoRepository) {
        this.transacaoRepository = transacaoRepository;
        this.extratoRepository = extratoRepository;
    }

    @Transactional
    public Transacao executar(Long id, String code) {
        Transacao transacao = transacaoRepository.buscarPorIdECode(id, code)
                .orElseThrow(() -> new TransacaoNaoEncontradaException(
                        "Transação não encontrada: id=" + id + ", code=" + code));

        transacao.confirmarRevisao();
        Transacao salva = transacaoRepository.salvar(transacao);

        if (transacao.getExtratoId() != null) {
            Extrato extrato = extratoRepository.buscarPorId(transacao.getExtratoId())
                    .orElseThrow(() -> new ExtratoInvalidoException(
                            "Extrato não encontrado: " + transacao.getExtratoId()));
            extrato.confirmarLancamento();
            extratoRepository.salvar(extrato);
        }

        return salva;
    }
}
