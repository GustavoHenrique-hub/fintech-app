package com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UsuarioRequestDTO(
        @NotBlank String cpf,
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String senha,
        String telefone,
        Long telegramChatId,
        Long whatsappChatId,
        Boolean emailVerificado,
        @NotNull LocalDate dataNascimento
) {
}
