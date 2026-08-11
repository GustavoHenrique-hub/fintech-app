package com.enterprise.gustadev.fintech_app.application.usuario.usecase;

import com.enterprise.gustadev.fintech_app.domain.usuario.exception.UsuarioInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;

/**
 * Atualiza os dados de contato do usuário (e-mail e telefone).
 * CPF é imutável por aqui — alteração de CPF exige ofício e execução direta no banco.
 */
public class AtualizarUsuarioUseCase {

    private final UsuarioRepositoryPort repository;

    public AtualizarUsuarioUseCase(UsuarioRepositoryPort repository) {
        this.repository = repository;
    }

    public Usuario executar(Long id, String email, String telefone) {
        Usuario usuario = repository.buscarPorId(id)
                .orElseThrow(() -> new UsuarioInvalidoException("Usuario nao encontrado"));

        if (email != null && !email.isBlank() && !email.equalsIgnoreCase(usuario.getEmail())) {
            repository.buscarPorEmail(email)
                    .filter(outro -> !outro.getIdUsuario().equals(id))
                    .ifPresent(outro -> {
                        throw new UsuarioInvalidoException("Email ja esta em uso por outro usuario");
                    });
            usuario.setEmail(email);
        }

        if (telefone != null) {
            usuario.setTelefone(telefone.isBlank() ? null : telefone);
        }

        usuario.validar();
        return repository.salvar(usuario);
    }
}
