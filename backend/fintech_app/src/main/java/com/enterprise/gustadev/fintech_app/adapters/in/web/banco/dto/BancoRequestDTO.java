package com.enterprise.gustadev.fintech_app.adapters.in.web.banco.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastrar um novo banco no catálogo.")
public record BancoRequestDTO(
        @Schema(description = "Nome do banco", example = "Nubank", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(max = 100) String nome,

        @Schema(description = "Descrição opcional do banco", example = "Banco digital brasileiro")
        @Size(max = 255) String descricao,

        @Schema(description = "Cor de destaque em hexadecimal", example = "#8A05BE")
        @Size(max = 7) String corHex,

        @Schema(description = "Identificador/nome do ícone para exibição", example = "nubank-icon")
        @Size(max = 50) String icone
) {}
