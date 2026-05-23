package com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoriadousuario;

import com.enterprise.gustadev.fintech_app.domain.categoriadousuario.model.CategoriaDoUsuario;
import com.enterprise.gustadev.fintech_app.domain.categoriadousuario.port.CategoriaDoUsuarioRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoriaDoUsuarioRepositoryAdapter implements CategoriaDoUsuarioRepositoryPort {

    private final CategoriaDoUsuarioJpaRepository jpaRepository;

    public CategoriaDoUsuarioRepositoryAdapter(CategoriaDoUsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public CategoriaDoUsuario salvar(CategoriaDoUsuario categoriaDoUsuario) {
        return jpaRepository.save(CategoriaDoUsuarioEntity.fromDomain(categoriaDoUsuario)).toDomain();
    }

    @Override
    public List<CategoriaDoUsuario> listarPorUsuario(Long usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId).stream()
                .map(CategoriaDoUsuarioEntity::toDomain).toList();
    }

    @Override
    public Optional<CategoriaDoUsuario> buscarPorUsuarioECategoria(Long usuarioId, Long categoriaId) {
        return jpaRepository.findByUsuarioIdAndCategoriaId(usuarioId, categoriaId)
                .map(CategoriaDoUsuarioEntity::toDomain);
    }

    @Override
    public void deletar(Long id) {
        jpaRepository.deleteById(id);
    }
}
