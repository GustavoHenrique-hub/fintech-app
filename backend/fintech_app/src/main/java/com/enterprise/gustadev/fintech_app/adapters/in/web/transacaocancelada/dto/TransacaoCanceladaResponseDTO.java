package com.enterprise.gustadev.fintech_app.adapters.in.web.transacaocancelada.dto;

import com.enterprise.gustadev.fintech_app.domain.transacaocancelada.model.TransacaoCancelada;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransacaoCanceladaResponseDTO(
        Long id,
        Long transacaoId,
        Long usuarioId,
        Long contaId,
        Long motivoId,
        String canceladoPor,
        BigDecimal valorOriginal,
        String observacao,
        OffsetDateTime canceladoEm
) {
    public static TransacaoCanceladaResponseDTO fromDomain(TransacaoCancelada domain) {
        return new TransacaoCanceladaResponseDTO(
                domain.getId(),
                domain.getTransacaoId(),
                domain.getUsuarioId(),
                domain.getContaId(),
                domain.getMotivoId(),
                domain.getCanceladoPor().name(),
                domain.getValorOriginal(),
                domain.getObservacao(),
                domain.getCanceladoEm()
        );
    }
}
