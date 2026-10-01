package com.enterprise.gustadev.fintech_app.application.usuario.usecase;

import com.enterprise.gustadev.fintech_app.domain.usuario.exception.VinculoTelegramInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.CodigoVinculoTelegram;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.CodigoVinculoTelegramRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.usuario.ports.UsuarioRepositoryPort;

/**
 * Consome o código vindo do bot e grava o chat_id no usuário dono do código.
 * Um chat pertence a um usuário só: se já estava ligado a outra conta, é movido.
 */
public class VincularTelegramUseCase {

    private static final String MENSAGEM_INVALIDO = "Código de vínculo inválido ou expirado";

    private final UsuarioRepositoryPort usuarioRepository;
    private final CodigoVinculoTelegramRepositoryPort codigoRepository;

    public VincularTelegramUseCase(UsuarioRepositoryPort usuarioRepository,
                                   CodigoVinculoTelegramRepositoryPort codigoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.codigoRepository = codigoRepository;
    }

    public Usuario executar(String codigo, Long chatId) {
        if (codigo == null || codigo.isBlank() || chatId == null) {
            throw new VinculoTelegramInvalidoException(MENSAGEM_INVALIDO);
        }
        CodigoVinculoTelegram vinculo = codigoRepository.buscarPorCodigo(codigo.trim().toUpperCase())
                .filter(c -> !c.foiUsado() && !c.expirou())
                .orElseThrow(() -> new VinculoTelegramInvalidoException(MENSAGEM_INVALIDO));

        Usuario usuario = usuarioRepository.buscarPorId(vinculo.getUsuarioId())
                .orElseThrow(() -> new VinculoTelegramInvalidoException(MENSAGEM_INVALIDO));

        usuarioRepository.buscarPorTelegramChatId(chatId)
                .filter(anterior -> !anterior.getIdUsuario().equals(usuario.getIdUsuario()))
                .ifPresent(anterior -> {
                    anterior.setTelegramChatID(null);
                    usuarioRepository.salvar(anterior);
                });

        vinculo.marcarUsado();
        codigoRepository.salvar(vinculo);

        usuario.setTelegramChatID(chatId);
        return usuarioRepository.salvar(usuario);
    }
}
