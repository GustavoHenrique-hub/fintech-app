package com.enterprise.gustadev.fintech_app.adapters.out.persistence.transacaocancelada;

import com.enterprise.gustadev.fintech_app.domain.transacaocancelada.model.TransacaoCancelada;
import com.enterprise.gustadev.fintech_app.domain.transacaocancelada.port.TransacaoCanceladaRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TransacaoCanceladaRepositoryAdapter implements TransacaoCanceladaRepositoryPort {

    private final TransacaoCanceladaJpaRepository jpaRepository;

    public TransacaoCanceladaRepositoryAdapter(TransacaoCanceladaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public TransacaoCancelada salvar(TransacaoCancelada transacaoCancelada) {
        return jpaRepository.save(TransacaoCanceladaEntity.fromDomain(transacaoCancelada)).toDomain();
    }

    @Override
    public List<TransacaoCancelada> listarPorUsuario(Long usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId).stream()
                .map(TransacaoCanceladaEntity::toDomain).toList();
    }

    @Override
    public Optional<TransacaoCancelada> buscarPorTransacao(Long transacaoId) {
        return jpaRepository.findByTransacaoId(transacaoId).map(TransacaoCanceladaEntity::toDomain);
    }
}
