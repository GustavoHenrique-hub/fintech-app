package com.enterprise.gustadev.fintech_app.adapters.out.persistence.snapshotfinanceiro;

import com.enterprise.gustadev.fintech_app.domain.snapshotfinanceiro.model.SnapshotFinanceiro;
import com.enterprise.gustadev.fintech_app.domain.snapshotfinanceiro.port.SnapshotFinanceiroRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SnapshotFinanceiroRepositoryAdapter implements SnapshotFinanceiroRepositoryPort {

    private final SnapshotFinanceiroJpaRepository jpaRepository;

    public SnapshotFinanceiroRepositoryAdapter(SnapshotFinanceiroJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SnapshotFinanceiro salvar(SnapshotFinanceiro snapshot) {
        return jpaRepository.save(SnapshotFinanceiroEntity.fromDomain(snapshot)).toDomain();
    }

    @Override
    public List<SnapshotFinanceiro> listarPorUsuario(Long usuarioId) {
        return jpaRepository.findByUsuarioIdOrderByAnoDescMesDesc(usuarioId)
                .stream().map(SnapshotFinanceiroEntity::toDomain).toList();
    }

    @Override
    public Optional<SnapshotFinanceiro> buscarPorUsuarioContaAnoMes(Long usuarioId, Long contaId, short ano, short mes) {
        return jpaRepository.findByUsuarioIdAndContaIdAndAnoAndMes(usuarioId, contaId, ano, mes)
                .map(SnapshotFinanceiroEntity::toDomain);
    }

    @Override
    public Optional<SnapshotFinanceiro> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(SnapshotFinanceiroEntity::toDomain);
    }
}
