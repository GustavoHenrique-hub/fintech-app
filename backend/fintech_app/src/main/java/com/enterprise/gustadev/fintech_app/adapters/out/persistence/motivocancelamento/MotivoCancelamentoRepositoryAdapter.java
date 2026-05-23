package com.enterprise.gustadev.fintech_app.adapters.out.persistence.motivocancelamento;

import com.enterprise.gustadev.fintech_app.domain.motivocancelamento.model.MotivoCancelamento;
import com.enterprise.gustadev.fintech_app.domain.motivocancelamento.port.MotivoCancelamentoRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MotivoCancelamentoRepositoryAdapter implements MotivoCancelamentoRepositoryPort {

    private final MotivoCancelamentoJpaRepository jpaRepository;

    public MotivoCancelamentoRepositoryAdapter(MotivoCancelamentoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public MotivoCancelamento salvar(MotivoCancelamento motivo) {
        return jpaRepository.save(MotivoCancelamentoEntity.fromDomain(motivo)).toDomain();
    }

    @Override
    public List<MotivoCancelamento> listarAtivos() {
        return jpaRepository.findByAtivoTrue().stream()
                .map(MotivoCancelamentoEntity::toDomain).toList();
    }

    @Override
    public Optional<MotivoCancelamento> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(MotivoCancelamentoEntity::toDomain);
    }

    @Override
    public Optional<MotivoCancelamento> buscarPorIdECode(Long id, String code) {
        return jpaRepository.findByIdAndCode(id, code).map(MotivoCancelamentoEntity::toDomain);
    }
}
