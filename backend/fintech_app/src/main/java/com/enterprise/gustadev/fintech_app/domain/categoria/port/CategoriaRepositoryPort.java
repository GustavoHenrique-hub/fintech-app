package com.enterprise.gustadev.fintech_app.domain.categoria.port;

import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepositoryPort {
    Categoria salvar(Categoria categoria);
    List<Categoria> listarPadrao();
    List<Categoria> listarPorTipo(TipoCategoria tipo);
    Optional<Categoria> buscarPorId(Long id);
    Optional<Categoria> buscarPorIdECode(Long id, String code);
}
