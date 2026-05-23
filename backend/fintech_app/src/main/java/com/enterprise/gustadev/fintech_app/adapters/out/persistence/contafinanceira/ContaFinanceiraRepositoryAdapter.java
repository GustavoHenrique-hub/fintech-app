package com.enterprise.gustadev.fintech_app.adapters.out.persistence.contafinanceira;

import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
    public List<ContaFinanceira> listarPorUsuario(Long usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId).stream().map(ContaFinanceiraEntity::toDomain).toList();
    }

    @Override
    public Optional<ContaFinanceira> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(ContaFinanceiraEntity::toDomain);
    }

    @Override
    public Optional<ContaFinanceira> buscarPorIdECode(Long id, String code) {
        return jpaRepository.findByIdAndCode(id, code).map(ContaFinanceiraEntity::toDomain);
    }

    @Override
    public void deletarPorId(Long id) {
        jpaRepository.deleteById(id);
    }
}
