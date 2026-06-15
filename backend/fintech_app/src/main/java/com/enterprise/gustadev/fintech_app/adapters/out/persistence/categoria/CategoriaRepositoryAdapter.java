package com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoria;

import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoriaRepositoryAdapter implements CategoriaRepositoryPort {

    private final CategoriaJpaRepository jpaRepository;

    public CategoriaRepositoryAdapter(CategoriaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Categoria salvar(Categoria categoria) {
        return jpaRepository.save(CategoriaEntity.fromDomain(categoria)).toDomain();
    }

    @Override
    public List<Categoria> listarPadrao() {
        return jpaRepository.findByPadraoTrue().stream().map(CategoriaEntity::toDomain).toList();
    }

    @Override
    public List<Categoria> listarPorTipo(TipoCategoria tipo) {
        return jpaRepository.findByTipo(tipo).stream().map(CategoriaEntity::toDomain).toList();
    }

    @Override
    public Optional<Categoria> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(CategoriaEntity::toDomain);
    }

    @Override
    public Optional<Categoria> buscarPorIdECode(Long id, String code) {
        return jpaRepository.findByIdAndCode(id, code).map(CategoriaEntity::toDomain);
    }

}
