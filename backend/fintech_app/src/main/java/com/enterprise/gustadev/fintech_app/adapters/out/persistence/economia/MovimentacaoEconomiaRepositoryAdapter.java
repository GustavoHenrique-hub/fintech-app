package com.enterprise.gustadev.fintech_app.adapters.out.persistence.economia;

import com.enterprise.gustadev.fintech_app.domain.economia.model.MovimentacaoEconomia;
import com.enterprise.gustadev.fintech_app.domain.economia.port.MovimentacaoEconomiaRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovimentacaoEconomiaRepositoryAdapter implements MovimentacaoEconomiaRepositoryPort {

    private final MovimentacaoEconomiaJpaRepository jpaRepository;

    public MovimentacaoEconomiaRepositoryAdapter(MovimentacaoEconomiaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public MovimentacaoEconomia salvar(MovimentacaoEconomia movimentacao) {
        return jpaRepository.save(MovimentacaoEconomiaEntity.fromDomain(movimentacao)).toDomain();
    }

    @Override
    public List<MovimentacaoEconomia> listarPorConta(Long contaId) {
        return jpaRepository.findByContaIdOrderByDataMovimentacaoDesc(contaId).stream()
                .map(MovimentacaoEconomiaEntity::toDomain).toList();
    }
}
