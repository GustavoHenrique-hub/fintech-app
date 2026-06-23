package com.enterprise.gustadev.fintech_app.adapters.out.persistence.sessaotoken;

import com.enterprise.gustadev.fintech_app.domain.auth.model.SessaoToken;
import com.enterprise.gustadev.fintech_app.domain.auth.port.SessaoTokenRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class SessaoTokenRepositoryAdapter implements SessaoTokenRepositoryPort {

    private final SessaoTokenJpaRepository jpaRepository;

    public SessaoTokenRepositoryAdapter(SessaoTokenJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SessaoToken salvar(SessaoToken sessao) {
        return jpaRepository.save(SessaoTokenEntity.fromDomain(sessao)).toDomain();
    }

    @Override
    public Optional<SessaoToken> buscarPorToken(String token) {
        return jpaRepository.findByToken(token).map(SessaoTokenEntity::toDomain);
    }

    @Override
    @Transactional
    public void deletarPorToken(String token) {
        jpaRepository.deleteByToken(token);
    }
}
