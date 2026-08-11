package com.enterprise.gustadev.fintech_app.application.extrato.usecase;

import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

public class RemoverExtratoUseCase {

    private final ExtratoRepositoryPort repository;

    public RemoverExtratoUseCase(ExtratoRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional
    public Extrato executar(Long id, String code) {
        Extrato extrato = repository.buscarPorIdECode(id, code)
                .orElseThrow(() -> new ExtratoInvalidoException(
                        "Extrato não encontrado: id=" + id + ", code=" + code));
        extrato.remover();
        return repository.salvar(extrato);
    }
}
