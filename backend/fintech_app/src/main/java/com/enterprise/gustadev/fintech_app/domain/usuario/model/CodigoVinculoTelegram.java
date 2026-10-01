package com.enterprise.gustadev.fintech_app.domain.usuario.model;

import lombok.Getter;
import lombok.Setter;

import java.security.SecureRandom;
import java.time.OffsetDateTime;

/**
 * Código de uso único que liga um chat do Telegram a um usuário do app.
 *
 * <p>O app gera o código e monta o deep link {@code t.me/<bot>?start=<codigo>}; o
 * Telegram entrega {@code /start <codigo>} ao bot, e o N8N devolve código + chat_id
 * para o backend. Nunca usar o {@code usuario_code} fixo aqui: quem o descobrisse
 * vincularia o próprio Telegram à conta de outra pessoa.
 */
@Getter
@Setter
public class CodigoVinculoTelegram {

    public static final int VALIDADE_MINUTOS = 10;

    // Sem 0/O e 1/I: o usuário pode precisar digitar o código em /vincular.
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int TAMANHO = 10;
    private static final SecureRandom RANDOM = new SecureRandom();

    private Long id;
    private String codigo;
    private Long usuarioId;
    private OffsetDateTime criadoEm;
    private OffsetDateTime expiraEm;
    private OffsetDateTime usadoEm;

    public CodigoVinculoTelegram(Long id, String codigo, Long usuarioId, OffsetDateTime criadoEm,
                                 OffsetDateTime expiraEm, OffsetDateTime usadoEm) {
        this.id = id;
        this.codigo = codigo;
        this.usuarioId = usuarioId;
        this.criadoEm = criadoEm;
        this.expiraEm = expiraEm;
        this.usadoEm = usadoEm;
    }

    /** Novo código para o usuário, válido por {@link #VALIDADE_MINUTOS}. */
    public static CodigoVinculoTelegram novo(Long usuarioId) {
        OffsetDateTime agora = OffsetDateTime.now();
        return new CodigoVinculoTelegram(null, gerarCodigo(), usuarioId, agora,
                agora.plusMinutes(VALIDADE_MINUTOS), null);
    }

    public boolean expirou() {
        return OffsetDateTime.now().isAfter(expiraEm);
    }

    public boolean foiUsado() {
        return usadoEm != null;
    }

    public void marcarUsado() {
        this.usadoEm = OffsetDateTime.now();
    }

    private static String gerarCodigo() {
        StringBuilder sb = new StringBuilder(TAMANHO);
        for (int i = 0; i < TAMANHO; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
