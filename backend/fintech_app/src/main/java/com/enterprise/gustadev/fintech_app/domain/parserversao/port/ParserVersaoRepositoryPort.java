package com.enterprise.gustadev.fintech_app.domain.parserversao.port;

import com.enterprise.gustadev.fintech_app.domain.parserversao.model.ParserVersao;

import java.util.List;
import java.util.Optional;

public interface ParserVersaoRepositoryPort {
    ParserVersao salvar(ParserVersao parserVersao);
    List<ParserVersao> listarTodos();
    List<ParserVersao> listarAtivos();
    Optional<ParserVersao> buscarPorId(Long id);
    Optional<ParserVersao> buscarPorBancoEVersao(String banco, String versao);
    void deletarPorId(Long id);
}
