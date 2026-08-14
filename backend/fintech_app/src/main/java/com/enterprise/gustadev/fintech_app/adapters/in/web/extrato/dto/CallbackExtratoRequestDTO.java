package com.enterprise.gustadev.fintech_app.adapters.in.web.extrato.dto;

import com.enterprise.gustadev.fintech_app.domain.extrato.model.ResultadoProcessamentoExtrato;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Corpo do callback enviado pelo N8N em {@code POST /extratos/{id}/callback} —
 * espelha o payload montado nos nós "Montar payload · sucesso/erro" do workflow
 * 01-extratos-core-ia. Campos extras (processamento, moeda, erro) são aceitos e
 * ignorados aqui: o que interessa ao domínio é status + metadados + lançamentos.
 */
public record CallbackExtratoRequestDTO(
        Long extratoId,
        String extratoCode,
        String status,
        String bancoDetectado,
        LocalDate periodoInicio,
        LocalDate periodoFim,
        Integer totalLancamentos,
        List<LancamentoDTO> transacoes
) {

    public record LancamentoDTO(
            LocalDate dataTransacao,
            String descricao,
            String estabelecimento,
            BigDecimal valor,
            String tipo,
            String categoriaSugerida,
            Short confiancaIa,
            String observacao
    ) {
        private ResultadoProcessamentoExtrato.LancamentoProcessado toDomain() {
            return new ResultadoProcessamentoExtrato.LancamentoProcessado(
                    dataTransacao, descricao, estabelecimento, valor,
                    direcao(), categoriaSugerida, confiancaIa, observacao);
        }

        /** Sem tipo reconhecível o lançamento é tratado como saída — o padrão do próprio workflow. */
        private TipoTransacao direcao() {
            if ("RECEITA".equalsIgnoreCase(String.valueOf(tipo))) return TipoTransacao.RECEITA;
            return TipoTransacao.GASTO;
        }
    }

    public ResultadoProcessamentoExtrato toDomain() {
        List<ResultadoProcessamentoExtrato.LancamentoProcessado> lancamentos =
                transacoes != null ? transacoes.stream().map(LancamentoDTO::toDomain).toList() : List.of();
        return new ResultadoProcessamentoExtrato(
                status, bancoDetectado, periodoInicio, periodoFim, lancamentos);
    }
}
