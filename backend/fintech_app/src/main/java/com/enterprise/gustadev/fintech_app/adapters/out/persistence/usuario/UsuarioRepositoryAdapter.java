package com.enterprise.gustadev.fintech_app.adapters.out.persistence.usuario;

import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioEntity entity = UsuarioEntity.fromDomain(usuario);
        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public List<Usuario> listarTodos() {
        return jpaRepository.findAll()
                .stream()
                .map(UsuarioEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(UsuarioEntity::toDomain);
    }

}
