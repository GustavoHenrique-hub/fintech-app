package com.enterprise.gustadev.fintech_app.domain.categoriapai.port;

import com.enterprise.gustadev.fintech_app.domain.categoriapai.model.CategoriaPai;

import java.util.List;
import java.util.Optional;

public interface CategoriaPaiRepositoryPort {
    CategoriaPai salvar(CategoriaPai categoriaPai);
    List<CategoriaPai> listarPorPai(Long paiId);
    Optional<CategoriaPai> buscarPorCategoria(Long categoriaId);
    void deletarPorCategoria(Long categoriaId);
}
