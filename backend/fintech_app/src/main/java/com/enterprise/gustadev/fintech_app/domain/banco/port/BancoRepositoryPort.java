package com.enterprise.gustadev.fintech_app.domain.banco.port;

import com.enterprise.gustadev.fintech_app.domain.banco.model.Banco;

import java.util.List;
import java.util.Optional;

public interface BancoRepositoryPort {
    Banco salvar(Banco banco);
    List<Banco> listarTodos();
    Optional<Banco> buscarPorId(Long id);
    Optional<Banco> buscarPorIdECode(Long id, String code);
    void deletarPorId(Long id);
}
