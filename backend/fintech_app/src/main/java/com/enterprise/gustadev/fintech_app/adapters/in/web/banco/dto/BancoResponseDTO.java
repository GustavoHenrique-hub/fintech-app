package com.enterprise.gustadev.fintech_app.adapters.in.web.banco.dto;

import com.enterprise.gustadev.fintech_app.domain.banco.model.Banco;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação de um banco do catálogo. O par (id, code) é usado para vincular contas.")
public record BancoResponseDTO(
        @Schema(description = "ID do banco (banco_id)", example = "10") Long id,
        @Schema(description = "Código alfanumérico de 6 caracteres (banco_code)", example = "NUBANK") String code,
        @Schema(description = "Nome do banco", example = "Nubank") String nome,
        @Schema(description = "Descrição opcional", example = "Banco digital brasileiro") String descricao,
        @Schema(description = "Cor de destaque em hexadecimal", example = "#8A05BE") String corHex,
        @Schema(description = "Identificador do ícone", example = "nubank-icon") String icone
) {
    public static BancoResponseDTO fromDomain(Banco domain) {
        return new BancoResponseDTO(
                domain.getId(),
                domain.getCode(),
                domain.getNome(),
                domain.getDescricao(),
                domain.getCorHex(),
                domain.getIcone()
        );
    }
}
