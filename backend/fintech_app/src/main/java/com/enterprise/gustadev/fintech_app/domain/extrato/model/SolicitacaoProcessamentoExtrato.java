package com.enterprise.gustadev.fintech_app.domain.extrato.model;

/**
 * Dados que o backend empurra para a automação externa (N8N) quando um extrato
 * precisa ser extraído/classificado por IA. É o contrato do webhook
 * {@code POST /webhook/extratos/processar} (ver automacao/n8n/02-extratos-entrada-app.json):
 * o arquivo vai em base64 e o processamento é fire-and-forget — o resultado volta
 * depois em {@code POST /extratos/{id}/callback}.
 */
public record SolicitacaoProcessamentoExtrato(
        Long extratoId,
        String extratoCode,
        Long usuarioId,
        Long contaId,
        String arquivoNome,
        String mimeType,
        byte[] conteudo,
        String origem
) {
}
