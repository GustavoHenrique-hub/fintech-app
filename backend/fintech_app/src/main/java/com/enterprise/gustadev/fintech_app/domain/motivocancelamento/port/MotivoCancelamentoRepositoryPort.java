package com.enterprise.gustadev.fintech_app.domain.motivocancelamento.port;

import com.enterprise.gustadev.fintech_app.domain.motivocancelamento.model.MotivoCancelamento;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MotivoCancelamentoRepositoryPort {
    MotivoCancelamento salvar(MotivoCancelamento motivo);
    List<MotivoCancelamento> listarAtivos();
    Optional<MotivoCancelamento> buscarPorId(UUID id);
}
