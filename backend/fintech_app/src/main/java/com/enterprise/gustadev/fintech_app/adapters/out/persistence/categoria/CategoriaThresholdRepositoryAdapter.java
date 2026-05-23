package com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoria;

import com.enterprise.gustadev.fintech_app.domain.categoria.model.CategoriaThreshold;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaThresholdRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CategoriaThresholdRepositoryAdapter implements CategoriaThresholdRepositoryPort {

    private final CategoriaThresholdJpaRepository jpaRepository;

    public CategoriaThresholdRepositoryAdapter(CategoriaThresholdJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public CategoriaThreshold salvar(CategoriaThreshold threshold) {
        return jpaRepository.save(CategoriaThresholdEntity.fromDomain(threshold)).toDomain();
    }

    @Override
    public Optional<CategoriaThreshold> buscarPorCategoriaId(UUID categoriaId) {
        return jpaRepository.findByCategoriaId(categoriaId).map(CategoriaThresholdEntity::toDomain);
    }

    @Override
    public void deletarPorId(UUID id) {
        jpaRepository.deleteById(id);
    }
}
