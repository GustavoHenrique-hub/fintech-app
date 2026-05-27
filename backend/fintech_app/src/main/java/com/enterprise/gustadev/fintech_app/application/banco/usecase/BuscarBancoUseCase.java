package com.enterprise.gustadev.fintech_app.application.banco.usecase;

import com.enterprise.gustadev.fintech_app.domain.banco.exception.BancoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.banco.model.Banco;
import com.enterprise.gustadev.fintech_app.domain.banco.port.BancoRepositoryPort;

public class BuscarBancoUseCase {

    private final BancoRepositoryPort repository;

    public BuscarBancoUseCase(BancoRepositoryPort repository) {
        this.repository = repository;
    }

    public Banco executar(Long id, String code) {
        return repository.buscarPorIdECode(id, code)
                .orElseThrow(() -> new BancoInvalidoException("Banco não encontrado: " + id));
    }
}
