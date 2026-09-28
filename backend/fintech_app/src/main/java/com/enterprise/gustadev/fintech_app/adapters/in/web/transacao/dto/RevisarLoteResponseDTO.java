package com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto;

import com.enterprise.gustadev.fintech_app.application.transacao.usecase.ConfirmarRevisaoTransacaoUseCase.FalhaRevisao;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.ConfirmarRevisaoTransacaoUseCase.ResultadoRevisaoLote;

import java.util.List;

public record RevisarLoteResponseDTO(List<TransacaoResponseDTO> revisadas, List<FalhaRevisao> falhas) {

    public static RevisarLoteResponseDTO fromDomain(ResultadoRevisaoLote resultado) {
        return new RevisarLoteResponseDTO(
                resultado.revisadas().stream().map(TransacaoResponseDTO::fromDomain).toList(),
                resultado.falhas());
    }
}
