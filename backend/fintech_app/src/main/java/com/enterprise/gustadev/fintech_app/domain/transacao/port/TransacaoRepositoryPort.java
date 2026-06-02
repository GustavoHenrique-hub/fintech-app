package com.enterprise.gustadev.fintech_app.domain.transacao.port;

import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransacaoRepositoryPort {
    Transacao salvar(Transacao transacao);
    List<Transacao> listarPorUsuario(Long usuarioId);
    List<Transacao> listarPorConta(Long contaId);
    List<Transacao> listarPorUsuarioNoPeriodoComCategoriaEConta(Long usuarioId, LocalDate inicio, LocalDate fim);
    Optional<Transacao> buscarPorId(Long id);
    Optional<Transacao> buscarPorIdECode(Long id, String code);
    void deletarPorId(Long id);
    Transacao estornaTransacao(Transacao transacao);
    Optional<Transacao> buscarTransacao(Long id, String code, Long usuarioId, String usuarioCode, Long contaId, String contaCode);
}
