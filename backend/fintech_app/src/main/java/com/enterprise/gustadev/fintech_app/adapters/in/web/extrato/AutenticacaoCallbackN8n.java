package com.enterprise.gustadev.fintech_app.adapters.in.web.extrato;

import com.enterprise.gustadev.fintech_app.domain.auth.exception.CredenciaisInvalidasException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

/**
 * Autentica as chamadas que o N8N faz de volta para a API ({@code /extratos/{id}/status}
 * e {@code /extratos/{id}/callback}). Essas rotas não passam pelo
 * {@code SessaoTokenFilter} — quem chama é a automação, não um usuário logado.
 *
 * <p>Dois níveis: a chave interna compartilhada ({@code X-Internal-Api-Key}) e, no
 * callback, a assinatura HMAC-SHA256 do corpo bruto ({@code X-N8N-Signature:
 * sha256=<hex>}), que garante que o payload não foi adulterado no caminho.
 *
 * <p>Quando os segredos não estão configurados (dev na máquina do desenvolvedor), a
 * verificação correspondente é pulada com um aviso no log — nunca silenciosamente.
 */
@Component
public class AutenticacaoCallbackN8n {

    private static final Logger log = LoggerFactory.getLogger(AutenticacaoCallbackN8n.class);
    private static final String PREFIXO_ASSINATURA = "sha256=";

    private final String internalApiKey;
    private final String callbackSecret;

    public AutenticacaoCallbackN8n(@Value("${n8n.internal-api-key:}") String internalApiKey,
                                    @Value("${n8n.callback-secret:}") String callbackSecret) {
        this.internalApiKey = internalApiKey;
        this.callbackSecret = callbackSecret;
    }

    public void exigirChaveInterna(String chaveRecebida) {
        if (internalApiKey == null || internalApiKey.isBlank()) {
            log.warn("n8n.internal-api-key não configurada — chamadas da automação não estão sendo autenticadas");
            return;
        }
        if (!iguais(internalApiKey, chaveRecebida)) {
            throw new CredenciaisInvalidasException("Chave interna inválida");
        }
    }

    public void exigirAssinatura(String corpoBruto, String assinaturaRecebida) {
        if (callbackSecret == null || callbackSecret.isBlank()) {
            log.warn("n8n.callback-secret não configurado — assinatura do callback não está sendo verificada");
            return;
        }
        if (assinaturaRecebida == null || !assinaturaRecebida.startsWith(PREFIXO_ASSINATURA)) {
            throw new CredenciaisInvalidasException("Assinatura do callback ausente ou mal formada");
        }
        String esperada = hmacSha256(corpoBruto);
        if (!iguais(esperada, assinaturaRecebida.substring(PREFIXO_ASSINATURA.length()))) {
            throw new CredenciaisInvalidasException("Assinatura do callback não confere");
        }
    }

    private String hmacSha256(String corpo) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(callbackSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return HexFormat.of().formatHex(mac.doFinal(corpo.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível calcular a assinatura do callback", e);
        }
    }

    /** Comparação de tempo constante — evita vazar o segredo por diferença de latência. */
    private static boolean iguais(String esperado, String recebido) {
        if (recebido == null) return false;
        return MessageDigest.isEqual(
                esperado.getBytes(StandardCharsets.UTF_8),
                recebido.getBytes(StandardCharsets.UTF_8));
    }
}
