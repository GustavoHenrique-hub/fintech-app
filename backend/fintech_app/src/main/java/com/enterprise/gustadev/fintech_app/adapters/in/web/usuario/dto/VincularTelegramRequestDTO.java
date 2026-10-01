package com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VincularTelegramRequestDTO(
        @NotBlank String codigo,
        @NotNull Long chatId
) {}
