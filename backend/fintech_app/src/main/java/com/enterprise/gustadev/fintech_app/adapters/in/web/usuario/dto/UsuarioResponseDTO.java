package com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto;

import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;

import java.time.LocalDate;

public record UsuarioResponseDTO(
        Long id,
        String usercode,
        String cpf,
        String nome,
        String email,
        String telefone,
        Long telegramChatId,
        Long whatsappChatId,
        Boolean emailVerificado,
        LocalDate dataNascimento
) {
    public static UsuarioResponseDTO fromDomain(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getUsuarioCode(),
                usuario.getCpf(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getTelegramChatID(),
                usuario.getWhatsappChatID(),
                usuario.getEmailVerificado(),
                usuario.getDtNascimento()
        );
    }
}
