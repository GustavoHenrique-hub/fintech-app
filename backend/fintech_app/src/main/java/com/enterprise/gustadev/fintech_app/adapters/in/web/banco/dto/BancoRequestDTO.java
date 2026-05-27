package com.enterprise.gustadev.fintech_app.adapters.in.web.banco.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BancoRequestDTO(
        @NotBlank @Size(max = 100) String nome,
        @Size(max = 255) String descricao,
        @Size(max = 7) String corHex,
        @Size(max = 50) String icone
) {}
