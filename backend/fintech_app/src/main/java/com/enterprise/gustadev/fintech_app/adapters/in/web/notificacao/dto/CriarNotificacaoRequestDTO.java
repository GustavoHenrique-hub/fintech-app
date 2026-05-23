package com.enterprise.gustadev.fintech_app.adapters.in.web.notificacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CriarNotificacaoRequestDTO(
        @NotNull Long usuarioId,
        @NotBlank String canal,
        @NotBlank String tipo,
        String titulo,
        String mensagem
) {}
