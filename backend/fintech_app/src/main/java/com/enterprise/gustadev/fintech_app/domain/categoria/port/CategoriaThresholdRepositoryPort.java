package com.enterprise.gustadev.fintech_app.domain.categoria.port;

import com.enterprise.gustadev.fintech_app.domain.categoria.model.CategoriaThreshold;

import java.util.Optional;
import java.util.UUID;

public interface CategoriaThresholdRepositoryPort {
    CategoriaThreshold salvar(CategoriaThreshold threshold);
    Optional<CategoriaThreshold> buscarPorCategoriaId(UUID categoriaId);
    void deletarPorId(UUID id);
}
