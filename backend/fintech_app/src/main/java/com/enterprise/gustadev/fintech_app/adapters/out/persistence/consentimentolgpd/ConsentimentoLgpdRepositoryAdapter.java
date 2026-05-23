package com.enterprise.gustadev.fintech_app.adapters.out.persistence.consentimentolgpd;

import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.model.ConsentimentoLgpd;
import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.port.ConsentimentoLgpdRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ConsentimentoLgpdRepositoryAdapter implements ConsentimentoLgpdRepositoryPort {

    private final ConsentimentoLgpdJpaRepository jpaRepository;

    public ConsentimentoLgpdRepositoryAdapter(ConsentimentoLgpdJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ConsentimentoLgpd salvar(ConsentimentoLgpd consentimento) {
        return jpaRepository.save(ConsentimentoLgpdEntity.fromDomain(consentimento)).toDomain();
    }

    @Override
    public List<ConsentimentoLgpd> listarPorUsuario(Long usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId).stream()
                .map(ConsentimentoLgpdEntity::toDomain).toList();
    }

    @Override
    public Optional<ConsentimentoLgpd> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(ConsentimentoLgpdEntity::toDomain);
    }
}
