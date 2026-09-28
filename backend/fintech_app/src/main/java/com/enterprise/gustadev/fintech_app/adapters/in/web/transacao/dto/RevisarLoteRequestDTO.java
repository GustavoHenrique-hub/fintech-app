package com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto;

import com.enterprise.gustadev.fintech_app.application.transacao.usecase.ConfirmarRevisaoTransacaoUseCase.ItemRevisao;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Corpo do {@code POST /transacoes/revisar-lote} ("Revisar tudo" na tela de revisão do
 * extrato): a escolha de cada lançamento, no mesmo formato do revisar individual.
 */
public record RevisarLoteRequestDTO(@NotEmpty @Valid List<Item> itens) {

    public record Item(@NotNull Long id, @NotNull String code,
                       String destino, Long categoriaId, String categoriaCode) {

        public ItemRevisao toDomain() {
            RevisarTransacaoRequestDTO escolha = new RevisarTransacaoRequestDTO(destino, categoriaId, categoriaCode);
            return new ItemRevisao(id, code, escolha.destinoDomain(), categoriaId, categoriaCode);
        }
    }
}
