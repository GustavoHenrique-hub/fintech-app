package com.enterprise.gustadev.fintech_app.adapters.in.web.extrato.dto;

import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ExtratoResponseDTO(
        UUID id,
        String code,
        UUID usuarioId,
        UUID contaId,
        String arquivoNome,
        String bancoDetectado,
        LocalDate periodoInicio,
        LocalDate periodoFim,
        String status,
        int totalLancamentos,
        int lancamentosConfirmados,
        int lancamentosPendentes,
        OffsetDateTime criadoEm
) {
    public static ExtratoResponseDTO fromDomain(Extrato domain) {
        return new ExtratoResponseDTO(
                domain.getId(),
                domain.getCode(),
                domain.getUsuarioId(),
                domain.getContaId(),
                domain.getArquivoNome(),
                domain.getBancoDetectado(),
                domain.getPeriodoInicio(),
                domain.getPeriodoFim(),
                domain.getStatus().name(),
                domain.getTotalLancamentos(),
                domain.getLancamentosConfirmados(),
                domain.getLancamentosPendentes(),
                domain.getCriadoEm()
        );
    }
}
