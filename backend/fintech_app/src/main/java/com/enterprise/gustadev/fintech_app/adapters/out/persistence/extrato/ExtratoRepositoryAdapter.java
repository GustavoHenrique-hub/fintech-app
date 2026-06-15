package com.enterprise.gustadev.fintech_app.adapters.out.persistence.extrato;

import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ExtratoRepositoryAdapter implements ExtratoRepositoryPort {

    private final ExtratoJpaRepository jpaRepository;

    public ExtratoRepositoryAdapter(ExtratoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Extrato salvar(Extrato extrato) {
        return jpaRepository.save(ExtratoEntity.fromDomain(extrato)).toDomain();
    }

    @Override
    public List<Extrato> listarPorUsuario(Long usuarioId) {
        return jpaRepository.findByUsuarioIdOrderByCriadoEmDesc(usuarioId)
                .stream().map(ExtratoEntity::toDomain).toList();
    }

    @Override
    public Optional<Extrato> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(ExtratoEntity::toDomain);
    }

    @Override
    public Optional<Extrato> buscarPorIdECode(Long id, String code) {
        return jpaRepository.findByIdAndCode(id, code).map(ExtratoEntity::toDomain);
    }

    @Override
    public Optional<Extrato> buscarPorHash(String hashArquivo) {
        return jpaRepository.findByHashArquivo(hashArquivo).map(ExtratoEntity::toDomain);
    }

}
