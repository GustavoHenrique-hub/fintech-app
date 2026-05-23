package com.enterprise.gustadev.fintech_app.domain.regraclassificacao.port;

import com.enterprise.gustadev.fintech_app.domain.regraclassificacao.model.RegraClassificacao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RegraClassificacaoRepositoryPort {
    RegraClassificacao salvar(RegraClassificacao regra);
    List<RegraClassificacao> listarPorUsuario(UUID usuarioId);
    Optional<RegraClassificacao> buscarPorId(UUID id);
    void deletarPorId(UUID id);
}
