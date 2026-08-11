package com.enterprise.gustadev.fintech_app.adapters.in.web.economia.dto;

import com.enterprise.gustadev.fintech_app.domain.economia.model.MovimentacaoEconomia;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Schema(description = "Registro de auditoria de uma movimentação do sub-saldo \"Economias\" (APORTE ou RESGATE).")
public record MovimentacaoEconomiaResponseDTO(
        @Schema(description = "ID da movimentação", example = "1") Long id,
        @Schema(description = "Código alfanumérico de 6 caracteres da movimentação", example = "ECN123") String code,
        @Schema(description = "ID da conta financeira dona da movimentação", example = "1") Long contaId,
        @Schema(description = "Code da conta financeira dona da movimentação", example = "ABC123") String contaCode,
        @Schema(description = "Tipo da movimentação", example = "APORTE",
                allowableValues = {"APORTE", "RESGATE"}) String tipo,
        @Schema(description = "Valor movimentado (sempre positivo)", example = "150.00") BigDecimal valor,
        @Schema(description = "Descrição livre informada pelo usuário") String descricao,
        @Schema(description = "Data/hora em que a movimentação ocorreu") OffsetDateTime dataMovimentacao,
        @Schema(description = "Data/hora de criação do registro") OffsetDateTime criadoEm
) {
    public static MovimentacaoEconomiaResponseDTO fromDomain(MovimentacaoEconomia domain) {
        return new MovimentacaoEconomiaResponseDTO(
                domain.getId(),
                domain.getCode(),
                domain.getContaId(),
                domain.getContaCode(),
                domain.getTipo() != null ? domain.getTipo().name() : null,
                domain.getValor(),
                domain.getDescricao(),
                domain.getDataMovimentacao(),
                domain.getCriadoEm()
        );
    }
}
