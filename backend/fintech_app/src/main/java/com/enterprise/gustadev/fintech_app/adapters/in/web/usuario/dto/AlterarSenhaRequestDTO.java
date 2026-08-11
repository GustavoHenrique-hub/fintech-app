package com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto;

import jakarta.validation.constraints.NotBlank;

public record AlterarSenhaRequestDTO(
        @NotBlank String senhaAtual,
        @NotBlank String novaSenha
) {
}
