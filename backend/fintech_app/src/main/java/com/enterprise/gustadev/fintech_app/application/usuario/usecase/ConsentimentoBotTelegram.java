package com.enterprise.gustadev.fintech_app.application.usuario.usecase;

import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.model.ConsentimentoLgpd;
import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.port.ConsentimentoLgpdRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConsentimentoLgpd;

import java.util.Comparator;

/**
 * Regra de negócio: canal externo só fala com o usuário se o consentimento
 * {@code bot_telegram} mais recente estiver aceito e não revogado.
 */
final class ConsentimentoBotTelegram {

    private ConsentimentoBotTelegram() {}

    static boolean ativo(ConsentimentoLgpdRepositoryPort repository, Long usuarioId) {
        return repository.listarPorUsuario(usuarioId).stream()
                .filter(c -> c.getTipo() == TipoConsentimentoLgpd.bot_telegram)
                .max(Comparator.comparing(ConsentimentoLgpd::getCriadoEm))
                .map(c -> c.isConsentido() && c.getRevogadoEm() == null)
                .orElse(false);
    }
}
