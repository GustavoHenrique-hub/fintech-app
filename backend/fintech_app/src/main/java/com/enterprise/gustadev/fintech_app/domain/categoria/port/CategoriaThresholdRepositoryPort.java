package com.enterprise.gustadev.fintech_app.domain.categoria.port;

import com.enterprise.gustadev.fintech_app.domain.categoria.model.CategoriaThreshold;

import java.util.Optional;

public interface CategoriaThresholdRepositoryPort {
    CategoriaThreshold salvar(CategoriaThreshold threshold);
    Optional<CategoriaThreshold> buscarPorCategoriaId(Long categoriaId);
    void deletarPorId(Long id);
}
