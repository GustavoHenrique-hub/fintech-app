package com.enterprise.gustadev.fintech_app.domain.processamentojob.port;

import com.enterprise.gustadev.fintech_app.domain.processamentojob.model.ProcessamentoJob;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusJob;

import java.util.List;
import java.util.Optional;

public interface ProcessamentoJobRepositoryPort {
    ProcessamentoJob salvar(ProcessamentoJob job);
    List<ProcessamentoJob> listarPorStatus(StatusJob status);
    List<ProcessamentoJob> listarPorExtrato(Long extratoId);
    Optional<ProcessamentoJob> buscarPorId(Long id);
}
