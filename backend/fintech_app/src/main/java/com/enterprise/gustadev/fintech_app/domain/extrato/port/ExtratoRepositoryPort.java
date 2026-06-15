package com.enterprise.gustadev.fintech_app.domain.extrato.port;

import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;

import java.util.List;
import java.util.Optional;

public interface ExtratoRepositoryPort {
    Extrato salvar(Extrato extrato);
    List<Extrato> listarPorUsuario(Long usuarioId);
    Optional<Extrato> buscarPorId(Long id);
    Optional<Extrato> buscarPorIdECode(Long id, String code);
    Optional<Extrato> buscarPorHash(String hashArquivo);
}
