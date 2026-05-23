package com.enterprise.gustadev.fintech_app.domain.categoria.port;

import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoriaRepositoryPort {
    Categoria salvar(Categoria categoria);
    List<Categoria> listarPadrao();
    List<Categoria> listarPorTipo(TipoCategoria tipo);
    Optional<Categoria> buscarPorId(UUID id);
    Optional<Categoria> buscarPorIdECode(UUID id, String code);
    void deletarPorId(UUID id);
}
