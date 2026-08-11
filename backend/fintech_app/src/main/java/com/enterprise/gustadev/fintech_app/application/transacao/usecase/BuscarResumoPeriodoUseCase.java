package com.enterprise.gustadev.fintech_app.application.transacao.usecase;

import com.enterprise.gustadev.fintech_app.domain.transacao.model.ResumoPeriodo;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;

import java.time.LocalDate;

public class BuscarResumoPeriodoUseCase {

    private final TransacaoRepositoryPort repository;

    public BuscarResumoPeriodoUseCase(TransacaoRepositoryPort repository) {
        this.repository = repository;
    }

    public ResumoPeriodo executar(Long usuarioId, String usuarioCode,
                                   Long contaId, String contaCode, LocalDate inicio, LocalDate fim) {
        return repository.somarPorUsuarioContaNoPeriodo(usuarioId, usuarioCode, contaId, contaCode, inicio, fim);
    }
}
