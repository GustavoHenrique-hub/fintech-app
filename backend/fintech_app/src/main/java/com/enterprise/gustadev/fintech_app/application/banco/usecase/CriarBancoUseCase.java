package com.enterprise.gustadev.fintech_app.application.banco.usecase;

import com.enterprise.gustadev.fintech_app.domain.banco.model.Banco;
import com.enterprise.gustadev.fintech_app.domain.banco.port.BancoRepositoryPort;

public class CriarBancoUseCase {

    private final BancoRepositoryPort repository;

    public CriarBancoUseCase(BancoRepositoryPort repository) {
        this.repository = repository;
    }

    public Banco executar(Banco banco) {
        banco.validar();
        return repository.salvar(banco);
    }
}
