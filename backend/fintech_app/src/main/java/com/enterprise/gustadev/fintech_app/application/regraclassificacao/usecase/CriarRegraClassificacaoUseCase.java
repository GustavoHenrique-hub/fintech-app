package com.enterprise.gustadev.fintech_app.application.regraclassificacao.usecase;

import com.enterprise.gustadev.fintech_app.domain.regraclassificacao.model.RegraClassificacao;
import com.enterprise.gustadev.fintech_app.domain.regraclassificacao.port.RegraClassificacaoRepositoryPort;

public class CriarRegraClassificacaoUseCase {

    private final RegraClassificacaoRepositoryPort repository;

    public CriarRegraClassificacaoUseCase(RegraClassificacaoRepositoryPort repository) {
        this.repository = repository;
    }

    public RegraClassificacao executar(RegraClassificacao regra) {
        regra.validar();
        return repository.salvar(regra);
    }
}
