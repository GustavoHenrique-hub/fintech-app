package com.enterprise.gustadev.fintech_app.domain.economia.port;

import com.enterprise.gustadev.fintech_app.domain.economia.model.MovimentacaoEconomia;

import java.util.List;

public interface MovimentacaoEconomiaRepositoryPort {
    MovimentacaoEconomia salvar(MovimentacaoEconomia movimentacao);

    /** Retorna todas as movimentações de economia da conta, ordenadas por dataMovimentacao desc. */
    List<MovimentacaoEconomia> listarPorConta(Long contaId);
}
