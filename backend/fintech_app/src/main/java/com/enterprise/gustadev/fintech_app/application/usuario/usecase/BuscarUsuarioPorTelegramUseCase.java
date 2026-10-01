package com.enterprise.gustadev.fintech_app.application.usuario.usecase;

import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.port.ConsentimentoLgpdRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.usuario.exception.UsuarioNaoVinculadoException;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;

import java.util.Comparator;

/**
 * Resolve quem está falando com o bot. Sem consentimento {@code bot_telegram} ativo o
 * chat é tratado como não vinculado — o bot não pode agir em nome do usuário.
 */
public class BuscarUsuarioPorTelegramUseCase {

    public record Resultado(Usuario usuario, Long contaPadraoId) {}

    private final UsuarioRepositoryPort usuarioRepository;
    private final ConsentimentoLgpdRepositoryPort consentimentoRepository;
    private final ContaFinanceiraRepositoryPort contaRepository;

    public BuscarUsuarioPorTelegramUseCase(UsuarioRepositoryPort usuarioRepository,
                                           ConsentimentoLgpdRepositoryPort consentimentoRepository,
                                           ContaFinanceiraRepositoryPort contaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.consentimentoRepository = consentimentoRepository;
        this.contaRepository = contaRepository;
    }

    public Resultado executar(Long chatId) {
        Usuario usuario = usuarioRepository.buscarPorTelegramChatId(chatId)
                .filter(u -> ConsentimentoBotTelegram.ativo(consentimentoRepository, u.getIdUsuario()))
                .orElseThrow(() -> new UsuarioNaoVinculadoException(
                        "Nenhuma conta vinculada ao chat " + chatId));

        // Conta marcada como padrão primeiro; sem ela, a ativa mais antiga.
        Long contaPadraoId = contaRepository.listarPorUsuario(usuario.getIdUsuario()).stream()
                .filter(ContaFinanceira::isAtiva)
                .min(Comparator.comparing((ContaFinanceira c) -> !c.isPadrao())
                        .thenComparing(ContaFinanceira::getId))
                .map(ContaFinanceira::getId)
                .orElse(null);

        return new Resultado(usuario, contaPadraoId);
    }
}
