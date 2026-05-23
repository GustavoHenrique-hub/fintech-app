package com.enterprise.gustadev.fintech_app.domain.contafinanceira.port;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContaFinanceiraRepositoryPort {
    ContaFinanceira salvar(ContaFinanceira conta);
    List<ContaFinanceira> listarPorUsuario(UUID usuarioId);
    Optional<ContaFinanceira> buscarPorId(UUID id);
    void deletarPorId(UUID id);
}
