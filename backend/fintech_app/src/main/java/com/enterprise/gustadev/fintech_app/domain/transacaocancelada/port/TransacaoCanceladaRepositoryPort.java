package com.enterprise.gustadev.fintech_app.domain.transacaocancelada.port;

import com.enterprise.gustadev.fintech_app.domain.transacaocancelada.model.TransacaoCancelada;

import java.util.List;
import java.util.Optional;

public interface TransacaoCanceladaRepositoryPort {
    TransacaoCancelada salvar(TransacaoCancelada transacaoCancelada);
    List<TransacaoCancelada> listarPorUsuario(Long usuarioId);
    Optional<TransacaoCancelada> buscarPorTransacao(Long transacaoId);
}
