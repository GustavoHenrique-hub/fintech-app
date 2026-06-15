package com.enterprise.gustadev.fintech_app.domain.contafinanceira.port;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;

import java.util.List;
import java.util.Optional;

public interface ContaFinanceiraRepositoryPort {
    ContaFinanceira salvar(ContaFinanceira conta);
    List<ContaFinanceira> listarPorUsuario(Long usuarioId);
    Optional<ContaFinanceira> buscarPorId(Long id);
    Optional<ContaFinanceira> buscarPorIdECode(Long id, String code);
}
