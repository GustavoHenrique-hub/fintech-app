package com.enterprise.gustadev.fintech_app.application.auth.usecase;

import com.enterprise.gustadev.fintech_app.domain.auth.exception.CredenciaisInvalidasException;
import com.enterprise.gustadev.fintech_app.domain.auth.model.SessaoToken;
import com.enterprise.gustadev.fintech_app.domain.auth.port.SenhaEncoder;
import com.enterprise.gustadev.fintech_app.domain.auth.port.SessaoTokenRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;

import java.time.LocalDateTime;
import java.util.UUID;

public class LoginUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final SessaoTokenRepositoryPort sessaoRepository;
    private final SenhaEncoder senhaEncoder;

    public LoginUseCase(UsuarioRepositoryPort usuarioRepository,
                        SessaoTokenRepositoryPort sessaoRepository,
                        SenhaEncoder senhaEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.sessaoRepository = sessaoRepository;
        this.senhaEncoder = senhaEncoder;
    }

    public SessaoToken executar(String email, String senha) {
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new CredenciaisInvalidasException("Email ou senha invalidos"));

        if (!senhaEncoder.matches(senha, usuario.getSenha())) {
            throw new CredenciaisInvalidasException("Email ou senha invalidos");
        }

        SessaoToken sessao = new SessaoToken(
                UUID.randomUUID().toString(),
                usuario.getIdUsuario(),
                usuario.getUsuarioCode(),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30)
        );

        return sessaoRepository.salvar(sessao);
    }
}
