package com.enterprise.gustadev.fintech_app.application.extrato.usecase;

import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusExtrato;
import org.springframework.transaction.annotation.Transactional;

/**
 * Atualiza o status de acompanhamento do extrato enquanto a automação trabalha
 * ({@code extraindo}, {@code classificando}...). Chamado pelo N8N em
 * {@code PATCH /extratos/{id}/status} — é só telemetria para a tela de extratos,
 * o resultado de verdade chega no callback.
 */
public class AtualizarStatusExtratoUseCase {

    private final ExtratoRepositoryPort repository;

    public AtualizarStatusExtratoUseCase(ExtratoRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional
    public Extrato executar(Long extratoId, String status) {
        Extrato extrato = repository.buscarPorId(extratoId)
                .orElseThrow(() -> new ExtratoInvalidoException("Extrato não encontrado: " + extratoId));

        StatusExtrato novoStatus;
        try {
            novoStatus = StatusExtrato.valueOf(String.valueOf(status).trim().toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new ExtratoInvalidoException("Status inválido: " + status);
        }

        // Um extrato já revisado não volta para "extraindo" por causa de um evento atrasado.
        if (!extrato.emProcessamento()) {
            return extrato;
        }

        extrato.setStatus(novoStatus);
        return repository.salvar(extrato);
    }
}
