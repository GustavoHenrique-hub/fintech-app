package com.enterprise.gustadev.fintech_app.domain.categoriadousuario.port;

import com.enterprise.gustadev.fintech_app.domain.categoriadousuario.model.CategoriaDoUsuario;

import java.util.List;
import java.util.Optional;

public interface CategoriaDoUsuarioRepositoryPort {
    CategoriaDoUsuario salvar(CategoriaDoUsuario categoriaDoUsuario);
    List<CategoriaDoUsuario> listarPorUsuario(Long usuarioId);
    Optional<CategoriaDoUsuario> buscarPorUsuarioECategoria(Long usuarioId, Long categoriaId);
    void deletar(Long id);
}
