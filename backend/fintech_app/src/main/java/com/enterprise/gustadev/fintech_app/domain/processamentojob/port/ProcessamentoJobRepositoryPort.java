package com.enterprise.gustadev.fintech_app.domain.processamentojob.port;

import com.enterprise.gustadev.fintech_app.domain.processamentojob.model.ProcessamentoJob;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusJob;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProcessamentoJobRepositoryPort {
    ProcessamentoJob salvar(ProcessamentoJob job);
    List<ProcessamentoJob> listarPorStatus(StatusJob status);
    List<ProcessamentoJob> listarPorExtrato(UUID extratoId);
    Optional<ProcessamentoJob> buscarPorId(UUID id);
    void deletarPorId(UUID id);
}
