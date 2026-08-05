package com.enterprise.gustadev.fintech_app.application.extrato.parser;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Um lançamento bruto lido de um extrato, antes de virar {@code Transacao}.
 * O sinal de {@code valor} já indica a direção (negativo = gasto, positivo = receita).
 */
public record LancamentoExtraido(LocalDate data, String descricao, BigDecimal valor) {
}
