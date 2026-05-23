package com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacao;

import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TransacaoRepositoryAdapter implements TransacaoRepositoryPort {

    private final TransacaoJpaRepository jpaRepository;

    public TransacaoRepositoryAdapter(TransacaoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Transacao salvar(Transacao transacao) {
        return jpaRepository.save(TransacaoEntity.fromDomain(transacao)).toDomain();
    }

    @Override
    public List<Transacao> listarPorUsuario(UUID usuarioId) {
        return jpaRepository.findByUsuarioIdAndDeletedAtIsNullOrderByDataTransacaoDesc(usuarioId)
                .stream().map(TransacaoEntity::toDomain).toList();
    }

    @Override
    public List<Transacao> listarPorConta(UUID contaId) {
        return jpaRepository.findByContaIdAndDeletedAtIsNull(contaId)
                .stream().map(TransacaoEntity::toDomain).toList();
    }

    @Override
    public List<Transacao> listarPorExtrato(UUID extratoId) {
        return jpaRepository.findByExtratoIdAndDeletedAtIsNull(extratoId)
                .stream().map(TransacaoEntity::toDomain).toList();
    }

    @Override
    public Optional<Transacao> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(TransacaoEntity::toDomain);
    }

    @Override
    public void deletarPorId(UUID id) {
        jpaRepository.deleteById(id);
    }
}
