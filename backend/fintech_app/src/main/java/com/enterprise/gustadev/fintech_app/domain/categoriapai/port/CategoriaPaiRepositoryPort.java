package com.enterprise.gustadev.fintech_app.domain.categoriapai.port;

import com.enterprise.gustadev.fintech_app.domain.categoriapai.model.CategoriaPai;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoriaPaiRepositoryPort {
    CategoriaPai salvar(CategoriaPai categoriaPai);
    List<CategoriaPai> listarPorPai(UUID paiId);
    Optional<CategoriaPai> buscarPorCategoria(UUID categoriaId);
    void deletarPorCategoria(UUID categoriaId);
}
