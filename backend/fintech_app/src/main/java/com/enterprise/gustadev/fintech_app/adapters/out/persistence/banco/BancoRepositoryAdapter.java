package com.enterprise.gustadev.fintech_app.adapters.out.persistence.banco;

import com.enterprise.gustadev.fintech_app.domain.banco.model.Banco;
import com.enterprise.gustadev.fintech_app.domain.banco.port.BancoRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BancoRepositoryAdapter implements BancoRepositoryPort {

    private final BancoJpaRepository jpaRepository;

    public BancoRepositoryAdapter(BancoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Banco salvar(Banco banco) {
        return jpaRepository.save(BancoEntity.fromDomain(banco)).toDomain();
    }

    @Override
    public List<Banco> listarTodos() {
        return jpaRepository.findAll().stream().map(BancoEntity::toDomain).toList();
    }

    @Override
    public Optional<Banco> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(BancoEntity::toDomain);
    }

    @Override
    public Optional<Banco> buscarPorIdECode(Long id, String code) {
        return jpaRepository.findByIdAndCode(id, code).map(BancoEntity::toDomain);
    }

    @Override
    public void deletarPorId(Long id) {
        jpaRepository.deleteById(id);
    }
}
