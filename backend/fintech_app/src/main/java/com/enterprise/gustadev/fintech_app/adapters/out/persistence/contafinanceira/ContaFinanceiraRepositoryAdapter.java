package com.enterprise.gustadev.fintech_app.adapters.out.persistence.contafinanceira;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ContaFinanceiraRepositoryAdapter implements ContaFinanceiraRepositoryPort {

    private final ContaFinanceiraJpaRepository jpaRepository;

    public ContaFinanceiraRepositoryAdapter(ContaFinanceiraJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ContaFinanceira salvar(ContaFinanceira conta) {
        return jpaRepository.save(ContaFinanceiraEntity.fromDomain(conta)).toDomain();
    }

    @Override
    public List<ContaFinanceira> listarPorUsuario(UUID usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId).stream().map(ContaFinanceiraEntity::toDomain).toList();
    }

    @Override
    public Optional<ContaFinanceira> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(ContaFinanceiraEntity::toDomain);
    }

    @Override
    public Optional<ContaFinanceira> buscarPorIdECode(UUID id, String code) {
        return jpaRepository.findByIdAndCode(id, code).map(ContaFinanceiraEntity::toDomain);
    }

    @Override
    public void deletarPorId(UUID id) {
        jpaRepository.deleteById(id);
    }
}
