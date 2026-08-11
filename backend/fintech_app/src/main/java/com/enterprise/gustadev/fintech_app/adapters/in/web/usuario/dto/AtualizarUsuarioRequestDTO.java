package com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto;

import jakarta.validation.constraints.Email;

/**
 * Atualização parcial dos dados de contato do usuário.
 * CPF é intencionalmente omitido deste DTO — é dado sensível e só pode ser
 * alterado via ofício, diretamente no banco de dados.
 */
public record AtualizarUsuarioRequestDTO(
        @Email String email,
        String telefone
) {
}
