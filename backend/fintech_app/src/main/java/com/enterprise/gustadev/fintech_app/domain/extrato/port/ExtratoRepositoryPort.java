package com.enterprise.gustadev.fintech_app.domain.extrato.port;

import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExtratoRepositoryPort {
    Extrato salvar(Extrato extrato);
    List<Extrato> listarPorUsuario(UUID usuarioId);
    Optional<Extrato> buscarPorId(UUID id);
    Optional<Extrato> buscarPorHash(String hashArquivo);
    void deletarPorId(UUID id);
}
