package com.enterprise.gustadev.fintech_app.adapters.in.web.motivocancelamento.dto;

import com.enterprise.gustadev.fintech_app.domain.motivocancelamento.model.MotivoCancelamento;

import java.util.UUID;

public record MotivoCancelamentoResponseDTO(
        UUID id,
        String descricao,
        String origemPermitida,
        boolean ativo
) {
    public static MotivoCancelamentoResponseDTO fromDomain(MotivoCancelamento domain) {
        return new MotivoCancelamentoResponseDTO(
                domain.getId(),
                domain.getDescricao(),
                domain.getOrigemPermitida().name(),
                domain.isAtivo()
        );
    }
}
