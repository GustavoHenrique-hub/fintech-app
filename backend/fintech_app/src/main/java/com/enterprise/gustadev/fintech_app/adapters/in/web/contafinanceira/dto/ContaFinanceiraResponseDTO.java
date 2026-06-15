package com.enterprise.gustadev.fintech_app.adapters.in.web.contafinanceira.dto;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Schema(description = "Representação de uma conta financeira retornada pela API, com o banco vinculado.")
public record ContaFinanceiraResponseDTO(
        @Schema(description = "ID da conta (id_contas)", example = "1") Long id,
        @Schema(description = "Código alfanumérico de 6 caracteres da conta (contas_code)", example = "ABC123") String code,
        @Schema(description = "ID do usuário dono da conta", example = "1") Long usuarioId,
        @Schema(description = "Tipo da conta", example = "corrente") String tipo,
        @Schema(description = "ID do banco vinculado", example = "10") Long bancoId,
        @Schema(description = "Código do banco vinculado", example = "NUBANK") String bancoCode,
        @Schema(description = "Saldo inicial registrado", example = "1500.00") BigDecimal saldoInicial,
        @Schema(description = "Indica se é a conta padrão do usuário") boolean padrao,
        @Schema(description = "Indica se a conta está ativa") boolean ativa,
        @Schema(description = "Indicador de remoção lógica (S=removida, N=ativa)") String indDelete,
        @Schema(description = "Data/hora de criação") OffsetDateTime criadoEm,
        @Schema(description = "Data/hora da remoção lógica") OffsetDateTime deletedAt
) {
    public static ContaFinanceiraResponseDTO fromDomain(ContaFinanceira domain) {
        return new ContaFinanceiraResponseDTO(
                domain.getId(),
                domain.getCode(),
                domain.getUsuarioId(),
                domain.getTipo().name(),
                domain.getBancoId(),
                domain.getBancoCode(),
                domain.getSaldoInicial(),
                domain.isPadrao(),
                domain.isAtiva(),
                domain.getIndDelete(),
                domain.getCriadoEm(),
                domain.getDeletedAt()
        );
    }
}
