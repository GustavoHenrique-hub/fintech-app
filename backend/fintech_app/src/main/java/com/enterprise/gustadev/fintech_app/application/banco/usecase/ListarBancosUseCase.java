package com.enterprise.gustadev.fintech_app.application.banco.usecase;

import com.enterprise.gustadev.fintech_app.domain.banco.model.Banco;
import com.enterprise.gustadev.fintech_app.domain.banco.port.BancoRepositoryPort;

import java.util.List;

public class ListarBancosUseCase {

    private final BancoRepositoryPort repository;

    public ListarBancosUseCase(BancoRepositoryPort repository) {
        this.repository = repository;
    }

    public List<Banco> executar() {
        return repository.listarTodos();
    }
}
