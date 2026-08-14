package com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.DestinoRevisaoLancamento;
import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoInvalidaException;

/**
 * Corpo (opcional) do {@code PATCH /transacoes/{id}/{code}/revisar}: a escolha que o
 * usuário faz na tela de revisão do extrato.
 *
 * @param destino      GASTO, RECEITA ou ECONOMIA. Ausente = mantém a classificação do extrato.
 * @param categoriaId  categoria escolhida para o lançamento (opcional, só para GASTO/RECEITA)
 */
public record RevisarTransacaoRequestDTO(
        String destino,
        Long categoriaId,
        String categoriaCode
) {

    public DestinoRevisaoLancamento destinoDomain() {
        if (destino == null || destino.isBlank()) return null;
        try {
            return DestinoRevisaoLancamento.valueOf(destino.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TransacaoInvalidaException(
                    "Destino inválido: " + destino + " (esperado GASTO, RECEITA ou ECONOMIA)");
        }
    }
}
