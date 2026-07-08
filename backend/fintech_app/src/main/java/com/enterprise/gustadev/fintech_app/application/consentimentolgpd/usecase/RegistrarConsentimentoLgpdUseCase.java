package com.enterprise.gustadev.fintech_app.application.consentimentolgpd.usecase;

import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.exception.ConsentimentoLgpdInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.model.ConsentimentoLgpd;
import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.port.ConsentimentoLgpdRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;

public class RegistrarConsentimentoLgpdUseCase {

    private final ConsentimentoLgpdRepositoryPort repository;
    private final UsuarioRepositoryPort usuarioRepository;

    public RegistrarConsentimentoLgpdUseCase(ConsentimentoLgpdRepositoryPort repository,
                                              UsuarioRepositoryPort usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public ConsentimentoLgpd executar(ConsentimentoLgpd consentimento) {
        Usuario usuario = usuarioRepository.buscarPorId(consentimento.getUsuarioId())
                .orElseThrow(() -> new ConsentimentoLgpdInvalidoException(
                        "Usuário não encontrado: id=" + consentimento.getUsuarioId()));
        consentimento.setUsuarioCode(usuario.getUsuarioCode());
        consentimento.validar();
        return repository.salvar(consentimento);
    }
}
