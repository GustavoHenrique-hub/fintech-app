package com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto;

import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;

import java.time.LocalDate;

public record UsuarioResponseDTO(
        Long id,
        String usercode,
        String cpf,
        String rg,
        String nome,
        String sobrenome,
        String email,
        LocalDate dataNascimento
) {
    public static UsuarioResponseDTO fromDomain(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsercode(),
                usuario.getCpf(),
                usuario.getRg(),
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getEmail(),
                usuario.getDataNascimento()
        );
    }
}
