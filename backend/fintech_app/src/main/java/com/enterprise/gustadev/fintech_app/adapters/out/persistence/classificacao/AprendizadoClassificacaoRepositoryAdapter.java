package com.enterprise.gustadev.fintech_app.adapters.out.persistence.classificacao;

import com.enterprise.gustadev.fintech_app.domain.classificacao.model.AprendizadoClassificacao;
import com.enterprise.gustadev.fintech_app.domain.classificacao.port.AprendizadoClassificacaoRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AprendizadoClassificacaoRepositoryAdapter implements AprendizadoClassificacaoRepositoryPort {

    private final AprendizadoClassificacaoJpaRepository jpaRepository;

    public AprendizadoClassificacaoRepositoryAdapter(AprendizadoClassificacaoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public AprendizadoClassificacao salvar(AprendizadoClassificacao aprendizado) {
        return jpaRepository.save(AprendizadoClassificacaoEntity.fromDomain(aprendizado)).toDomain();
    }

    @Override
    public Optional<AprendizadoClassificacao> buscarPorUsuarioEChave(Long usuarioId, String chave) {
        return jpaRepository.findByUsuarioIdAndChave(usuarioId, chave)
                .map(AprendizadoClassificacaoEntity::toDomain);
    }
}
