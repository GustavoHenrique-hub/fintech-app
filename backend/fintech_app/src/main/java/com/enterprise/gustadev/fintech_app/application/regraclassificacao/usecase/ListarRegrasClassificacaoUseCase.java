package com.enterprise.gustadev.fintech_app.application.regraclassificacao.usecase;

import com.enterprise.gustadev.fintech_app.domain.regraclassificacao.model.RegraClassificacao;
import com.enterprise.gustadev.fintech_app.domain.regraclassificacao.port.RegraClassificacaoRepositoryPort;

import java.util.List;
import java.util.UUID;

public class ListarRegrasClassificacaoUseCase {

    private final RegraClassificacaoRepositoryPort repository;

    public ListarRegrasClassificacaoUseCase(RegraClassificacaoRepositoryPort repository) {
        this.repository = repository;
    }

    public List<RegraClassificacao> executar(UUID usuarioId) {
        return repository.listarPorUsuario(usuarioId);
    }
}
