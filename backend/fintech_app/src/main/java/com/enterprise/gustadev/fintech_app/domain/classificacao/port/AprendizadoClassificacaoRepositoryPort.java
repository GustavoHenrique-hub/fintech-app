package com.enterprise.gustadev.fintech_app.domain.classificacao.port;

import com.enterprise.gustadev.fintech_app.domain.classificacao.model.AprendizadoClassificacao;

import java.util.Optional;

public interface AprendizadoClassificacaoRepositoryPort {
    AprendizadoClassificacao salvar(AprendizadoClassificacao aprendizado);
    Optional<AprendizadoClassificacao> buscarPorUsuarioEChave(Long usuarioId, String chave);
}
