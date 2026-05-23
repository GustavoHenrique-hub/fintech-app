package com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoriapai;

import com.enterprise.gustadev.fintech_app.domain.categoriapai.model.CategoriaPai;
import com.enterprise.gustadev.fintech_app.domain.categoriapai.port.CategoriaPaiRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoriaPaiRepositoryAdapter implements CategoriaPaiRepositoryPort {

    private final CategoriaPaiJpaRepository jpaRepository;

    public CategoriaPaiRepositoryAdapter(CategoriaPaiJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public CategoriaPai salvar(CategoriaPai categoriaPai) {
        return jpaRepository.save(CategoriaPaiEntity.fromDomain(categoriaPai)).toDomain();
    }

    @Override
    public List<CategoriaPai> listarPorPai(Long paiId) {
        return jpaRepository.findByPaiId(paiId).stream().map(CategoriaPaiEntity::toDomain).toList();
    }

    @Override
    public Optional<CategoriaPai> buscarPorCategoria(Long categoriaId) {
        return jpaRepository.findByCategoriaId(categoriaId).map(CategoriaPaiEntity::toDomain);
    }

    @Override
    public void deletarPorCategoria(Long categoriaId) {
        jpaRepository.deleteByCategoriaId(categoriaId);
    }
}
