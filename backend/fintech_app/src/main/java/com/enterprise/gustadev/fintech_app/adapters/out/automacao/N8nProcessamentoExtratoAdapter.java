package com.enterprise.gustadev.fintech_app.adapters.out.automacao;

import com.enterprise.gustadev.fintech_app.domain.extrato.model.SolicitacaoProcessamentoExtrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ProcessamentoExtratoPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Empurra o extrato recém-recebido para o webhook do N8N
 * ({@code POST /webhook/extratos/processar}, workflow 02-extratos-entrada-app).
 *
 * <p>É fire-and-forget: o N8N responde 202 imediatamente e devolve o resultado
 * depois em {@code POST /extratos/{id}/callback}. Qualquer falha aqui (automação
 * desligada, N8N fora do ar, chave errada) retorna {@code false} em vez de
 * propagar exceção — quem chama decide o fallback (parser local).
 */
@Component
public class N8nProcessamentoExtratoAdapter implements ProcessamentoExtratoPort {

    private static final Logger log = LoggerFactory.getLogger(N8nProcessamentoExtratoAdapter.class);

    private final boolean habilitado;
    private final String webhookUrl;
    private final String internalApiKey;
    private final RestClient restClient;

    public N8nProcessamentoExtratoAdapter(
            @Value("${n8n.enabled:true}") boolean habilitado,
            @Value("${n8n.webhook-url:http://localhost:5678/webhook/extratos/processar}") String webhookUrl,
            @Value("${n8n.internal-api-key:}") String internalApiKey,
            @Value("${n8n.timeout-ms:10000}") long timeoutMs) {
        this.habilitado = habilitado;
        this.webhookUrl = webhookUrl;
        this.internalApiKey = internalApiKey;

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(timeoutMs));
        factory.setReadTimeout(Duration.ofMillis(timeoutMs));
        this.restClient = RestClient.builder().requestFactory(factory).build();
    }

    @Override
    public boolean enviarParaProcessamento(SolicitacaoProcessamentoExtrato solicitacao) {
        if (!habilitado) {
            log.debug("Automação N8N desabilitada (n8n.enabled=false) — extrato {} segue no parser local",
                    solicitacao.extratoId());
            return false;
        }

        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("extratoId", solicitacao.extratoId());
        corpo.put("extratoCode", solicitacao.extratoCode());
        corpo.put("usuarioId", solicitacao.usuarioId());
        corpo.put("contaId", solicitacao.contaId());
        corpo.put("arquivoNome", solicitacao.arquivoNome());
        corpo.put("mimeType", solicitacao.mimeType());
        corpo.put("arquivoBase64", Base64.getEncoder().encodeToString(solicitacao.conteudo()));
        corpo.put("origem", solicitacao.origem());

        try {
            restClient.post()
                    .uri(webhookUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Internal-Api-Key", internalApiKey)
                    .body(corpo)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Extrato {} enviado para a automação N8N ({})", solicitacao.extratoId(), webhookUrl);
            return true;
        } catch (Exception e) {
            log.warn("Falha ao enviar o extrato {} para o N8N ({}): {} — caindo no parser local",
                    solicitacao.extratoId(), webhookUrl, e.getMessage());
            return false;
        }
    }
}
