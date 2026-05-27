package com.enterprise.gustadev.fintech_app.application.banco.usecase;

import com.enterprise.gustadev.fintech_app.domain.banco.exception.BancoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.banco.port.BancoRepositoryPort;

public class DeletarBancoUseCase {

    private final BancoRepositoryPort repository;

    public DeletarBancoUseCase(BancoRepositoryPort repository) {
        this.repository = repository;
    }

    public void executar(Long id, String code) {
        repository.buscarPorIdECode(id, code)
                .orElseThrow(() -> new BancoInvalidoException("Banco não encontrado: " + id));
        repository.deletarPorId(id);
    }
}
