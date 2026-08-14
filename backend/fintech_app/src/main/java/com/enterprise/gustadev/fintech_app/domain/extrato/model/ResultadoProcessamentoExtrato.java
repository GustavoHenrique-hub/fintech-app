package com.enterprise.gustadev.fintech_app.domain.extrato.model;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Resultado que a automação externa devolve depois de ler e classificar o extrato
 * ({@code POST /extratos/{id}/callback}). É o espelho, já em tipos do domínio, do
 * payload montado pelo workflow 01-extratos-core-ia.
 *
 * @param status      status final reportado pela automação (ex.: {@code pendente_revisao}, {@code erro_extracao})
 * @param lancamentos vazio quando o processamento terminou em erro
 */
public record ResultadoProcessamentoExtrato(
        String status,
        String bancoDetectado,
        LocalDate periodoInicio,
        LocalDate periodoFim,
        List<LancamentoProcessado> lancamentos
) {

    /**
     * Um lançamento já normalizado pela automação: {@code valor} sempre positivo,
     * a direção vive em {@code tipo}.
     */
    public record LancamentoProcessado(
            LocalDate dataTransacao,
            String descricao,
            String estabelecimento,
            BigDecimal valor,
            TipoTransacao tipo,
            String categoriaSugerida,
            Short confiancaIa,
            String observacao
    ) {
    }
}
