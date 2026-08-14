package com.enterprise.gustadev.fintech_app.domain.extrato.port;

import com.enterprise.gustadev.fintech_app.domain.extrato.model.SolicitacaoProcessamentoExtrato;

/**
 * Porta de saída para a automação que extrai e classifica os lançamentos do
 * extrato (hoje o N8N + Claude). O domínio não sabe qual automação está por trás.
 */
public interface ProcessamentoExtratoPort {

    /** {@code true} quando a automação está habilitada e aceitou a solicitação. */
    boolean enviarParaProcessamento(SolicitacaoProcessamentoExtrato solicitacao);
}
