package com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto;

import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;

/** Visão mínima do usuário para a automação — sem CPF, e-mail ou senha. */
public record UsuarioTelegramResponseDTO(
        Long usuarioId,
        String usuarioCode,
        String nome,
        Long chatId,
        Long contaPadraoId
) {
    public static UsuarioTelegramResponseDTO of(Usuario usuario, Long contaPadraoId) {
        return new UsuarioTelegramResponseDTO(
                usuario.getIdUsuario(),
                usuario.getUsuarioCode(),
                usuario.getNome(),
                usuario.getTelegramChatID(),
                contaPadraoId
        );
    }
}
