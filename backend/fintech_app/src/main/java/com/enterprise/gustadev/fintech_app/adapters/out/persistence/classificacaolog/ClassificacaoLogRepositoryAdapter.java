package com.enterprise.gustadev.fintech_app.adapters.out.persistence.classificacaolog;

import com.enterprise.gustadev.fintech_app.domain.classificacaolog.model.ClassificacaoLog;
import com.enterprise.gustadev.fintech_app.domain.classificacaolog.port.ClassificacaoLogRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ClassificacaoLogRepositoryAdapter implements ClassificacaoLogRepositoryPort {

    private final ClassificacaoLogJpaRepository jpaRepository;

    public ClassificacaoLogRepositoryAdapter(ClassificacaoLogJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ClassificacaoLog salvar(ClassificacaoLog log) {
        return jpaRepository.save(ClassificacaoLogEntity.fromDomain(log)).toDomain();
    }

    @Override
    public List<ClassificacaoLog> listarPorTransacao(UUID transacaoId) {
        return jpaRepository.findByTransacaoId(transacaoId).stream()
                .map(ClassificacaoLogEntity::toDomain).toList();
    }

    @Override
    public List<ClassificacaoLog> listarPorUsuario(UUID usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId).stream()
                .map(ClassificacaoLogEntity::toDomain).toList();
    }

    @Override
    public Optional<ClassificacaoLog> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(ClassificacaoLogEntity::toDomain);
    }
}
