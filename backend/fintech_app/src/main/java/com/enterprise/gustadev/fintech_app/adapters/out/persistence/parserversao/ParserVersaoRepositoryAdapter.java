package com.enterprise.gustadev.fintech_app.adapters.out.persistence.parserversao;

import com.enterprise.gustadev.fintech_app.domain.parserversao.model.ParserVersao;
import com.enterprise.gustadev.fintech_app.domain.parserversao.port.ParserVersaoRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ParserVersaoRepositoryAdapter implements ParserVersaoRepositoryPort {

    private final ParserVersaoJpaRepository jpaRepository;

    public ParserVersaoRepositoryAdapter(ParserVersaoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ParserVersao salvar(ParserVersao parserVersao) {
        return jpaRepository.save(ParserVersaoEntity.fromDomain(parserVersao)).toDomain();
    }

    @Override
    public List<ParserVersao> listarTodos() {
        return jpaRepository.findAll().stream().map(ParserVersaoEntity::toDomain).toList();
    }

    @Override
    public List<ParserVersao> listarAtivos() {
        return jpaRepository.findByAtivoTrue().stream().map(ParserVersaoEntity::toDomain).toList();
    }

    @Override
    public Optional<ParserVersao> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(ParserVersaoEntity::toDomain);
    }

    @Override
    public Optional<ParserVersao> buscarPorBancoEVersao(String banco, String versao) {
        return jpaRepository.findByBancoAndVersao(banco, versao).map(ParserVersaoEntity::toDomain);
    }

    @Override
    public void deletarPorId(UUID id) {
        jpaRepository.deleteById(id);
    }
}
