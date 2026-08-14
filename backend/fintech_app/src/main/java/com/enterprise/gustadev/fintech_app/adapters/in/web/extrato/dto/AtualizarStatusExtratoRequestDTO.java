package com.enterprise.gustadev.fintech_app.adapters.in.web.extrato.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Corpo do {@code PATCH /extratos/{id}/status} enviado pela automação enquanto
 * processa o arquivo. {@code origem} e {@code execucaoN8n} vêm no payload do N8N
 * apenas para rastreio no log de acesso.
 */
public record AtualizarStatusExtratoRequestDTO(
        @NotBlank(message = "Status é obrigatório") String status,
        String origem,
        String execucaoN8n
) {
}
