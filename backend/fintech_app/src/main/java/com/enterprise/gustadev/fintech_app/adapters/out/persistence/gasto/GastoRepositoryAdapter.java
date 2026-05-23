package com.enterprise.gustadev.fintech_app.adapters.out.persistence.gasto;

import com.enterprise.gustadev.fintech_app.domain.gasto.model.Gasto;
import com.enterprise.gustadev.fintech_app.domain.gasto.port.GastoRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GastoRepositoryAdapter implements GastoRepositoryPort {

    private final GastoJpaRepository jpaRepository;

    public GastoRepositoryAdapter(GastoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Gasto salvar(Gasto gasto) {
        GastoEntity entity = GastoEntity.fromDomain(gasto);
        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public List<Gasto> listarPorUsuario(Long usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(GastoEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Gasto> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(GastoEntity::toDomain);
    }

    @Override
    public void deletarPorId(Long id) {
        jpaRepository.deleteById(id);
    }
}
