package com.enterprise.gustadev.fintech_app.domain.usuario.ports;

import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryPort {

    Usuario salvar(Usuario usuario);

    List<Usuario> listarTodos();

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorEmail(String email);

}
