package com.enterprise.gustadev.fintech_app.domain.classificacaolog.port;

import com.enterprise.gustadev.fintech_app.domain.classificacaolog.model.ClassificacaoLog;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClassificacaoLogRepositoryPort {
    ClassificacaoLog salvar(ClassificacaoLog log);
    List<ClassificacaoLog> listarPorTransacao(UUID transacaoId);
    List<ClassificacaoLog> listarPorUsuario(UUID usuarioId);
    Optional<ClassificacaoLog> buscarPorId(UUID id);
}
