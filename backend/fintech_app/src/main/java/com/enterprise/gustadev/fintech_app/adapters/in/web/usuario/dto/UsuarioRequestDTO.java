package com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UsuarioRequestDTO(
        String usercode,
        @NotBlank String cpf,
        @NotBlank String rg,
        @NotBlank String nome,
        @NotBlank String sobrenome,
        @NotBlank @Email String email,
        @NotBlank String senha,
        @NotNull LocalDate dataNascimento
) {
}
