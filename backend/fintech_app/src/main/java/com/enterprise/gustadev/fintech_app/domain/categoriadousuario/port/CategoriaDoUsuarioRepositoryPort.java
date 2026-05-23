package com.enterprise.gustadev.fintech_app.domain.categoriadousuario.port;

import com.enterprise.gustadev.fintech_app.domain.categoriadousuario.model.CategoriaDoUsuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoriaDoUsuarioRepositoryPort {
    CategoriaDoUsuario salvar(CategoriaDoUsuario categoriaDoUsuario);
    List<CategoriaDoUsuario> listarPorUsuario(UUID usuarioId);
    Optional<CategoriaDoUsuario> buscarPorUsuarioECategoria(UUID usuarioId, UUID categoriaId);
    void deletar(UUID id);
}
