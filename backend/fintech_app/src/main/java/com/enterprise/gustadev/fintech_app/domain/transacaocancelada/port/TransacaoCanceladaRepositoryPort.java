package com.enterprise.gustadev.fintech_app.domain.transacaocancelada.port;

import com.enterprise.gustadev.fintech_app.domain.transacaocancelada.model.TransacaoCancelada;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransacaoCanceladaRepositoryPort {
    TransacaoCancelada salvar(TransacaoCancelada transacaoCancelada);
    List<TransacaoCancelada> listarPorUsuario(UUID usuarioId);
    Optional<TransacaoCancelada> buscarPorTransacao(UUID transacaoId);
}
