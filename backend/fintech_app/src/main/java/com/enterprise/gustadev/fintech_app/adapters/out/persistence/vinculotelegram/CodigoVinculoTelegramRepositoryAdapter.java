package com.enterprise.gustadev.fintech_app.adapters.out.persistence.vinculotelegram;

import com.enterprise.gustadev.fintech_app.domain.usuario.model.CodigoVinculoTelegram;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.CodigoVinculoTelegramRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CodigoVinculoTelegramRepositoryAdapter implements CodigoVinculoTelegramRepositoryPort {

    private final CodigoVinculoTelegramJpaRepository jpaRepository;

    public CodigoVinculoTelegramRepositoryAdapter(CodigoVinculoTelegramJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public CodigoVinculoTelegram salvar(CodigoVinculoTelegram codigo) {
        return jpaRepository.save(CodigoVinculoTelegramEntity.fromDomain(codigo)).toDomain();
    }

    @Override
    public Optional<CodigoVinculoTelegram> buscarPorCodigo(String codigo) {
        return jpaRepository.findByCodigo(codigo).map(CodigoVinculoTelegramEntity::toDomain);
    }
}
