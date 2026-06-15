package com.enterprise.gustadev.fintech_app.adapters.out.persistence.processamentojob;

import com.enterprise.gustadev.fintech_app.domain.processamentojob.model.ProcessamentoJob;
import com.enterprise.gustadev.fintech_app.domain.processamentojob.port.ProcessamentoJobRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusJob;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProcessamentoJobRepositoryAdapter implements ProcessamentoJobRepositoryPort {

    private final ProcessamentoJobJpaRepository jpaRepository;

    public ProcessamentoJobRepositoryAdapter(ProcessamentoJobJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ProcessamentoJob salvar(ProcessamentoJob job) {
        return jpaRepository.save(ProcessamentoJobEntity.fromDomain(job)).toDomain();
    }

    @Override
    public List<ProcessamentoJob> listarPorStatus(StatusJob status) {
        return jpaRepository.findByStatus(status).stream()
                .map(ProcessamentoJobEntity::toDomain).toList();
    }

    @Override
    public List<ProcessamentoJob> listarPorExtrato(Long extratoId) {
        return jpaRepository.findByExtratoId(extratoId).stream()
                .map(ProcessamentoJobEntity::toDomain).toList();
    }

    @Override
    public Optional<ProcessamentoJob> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(ProcessamentoJobEntity::toDomain);
    }

}
