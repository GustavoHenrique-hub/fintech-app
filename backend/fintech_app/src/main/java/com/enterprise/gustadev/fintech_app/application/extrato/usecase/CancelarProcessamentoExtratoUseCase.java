package com.enterprise.gustadev.fintech_app.application.extrato.usecase;

import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tira do processamento um extrato que o usuário desistiu de esperar (automação
 * travada, fila parada...), deixando-o em {@code erro_classificacao} para poder ser
 * reenviado. Não interrompe a execução no N8N — só faz o backend descartar o
 * callback que ela ainda possa mandar.
 */
public class CancelarProcessamentoExtratoUseCase {

    private final ExtratoRepositoryPort repository;

    public CancelarProcessamentoExtratoUseCase(ExtratoRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional
    public Extrato executar(Long id, String code) {
        Extrato extrato = repository.buscarPorIdECode(id, code)
                .orElseThrow(() -> new ExtratoInvalidoException(
                        "Extrato não encontrado: id=" + id + ", code=" + code));
        extrato.cancelarProcessamento();
        return repository.salvar(extrato);
    }
}
