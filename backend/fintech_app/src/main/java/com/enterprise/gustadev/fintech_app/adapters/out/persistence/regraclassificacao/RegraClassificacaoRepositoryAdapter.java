package com.enterprise.gustadev.fintech_app.adapters.out.persistence.regraclassificacao;

import com.enterprise.gustadev.fintech_app.domain.regraclassificacao.model.RegraClassificacao;
import com.enterprise.gustadev.fintech_app.domain.regraclassificacao.port.RegraClassificacaoRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RegraClassificacaoRepositoryAdapter implements RegraClassificacaoRepositoryPort {

    private final RegraClassificacaoJpaRepository jpaRepository;

    public RegraClassificacaoRepositoryAdapter(RegraClassificacaoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public RegraClassificacao salvar(RegraClassificacao regra) {
        return jpaRepository.save(RegraClassificacaoEntity.fromDomain(regra)).toDomain();
    }

    @Override
    public List<RegraClassificacao> listarPorUsuario(UUID usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId).stream()
                .map(RegraClassificacaoEntity::toDomain).toList();
    }

    @Override
    public Optional<RegraClassificacao> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(RegraClassificacaoEntity::toDomain);
    }

    @Override
    public void deletarPorId(UUID id) {
        jpaRepository.deleteById(id);
    }
}
