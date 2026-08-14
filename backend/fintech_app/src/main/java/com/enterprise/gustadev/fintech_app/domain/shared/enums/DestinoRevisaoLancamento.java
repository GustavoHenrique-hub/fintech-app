package com.enterprise.gustadev.fintech_app.domain.shared.enums;

/**
 * Para onde o usuário manda um lançamento importado ao revisá-lo na tela de extratos.
 *
 * <p>{@code GASTO} e {@code RECEITA} confirmam a transação (ela vive na aba
 * "Transações"); {@code ECONOMIA} tira o lançamento de receitas/gastos e o
 * converte numa movimentação do sub-saldo de economias da conta.
 */
public enum DestinoRevisaoLancamento {
    GASTO, RECEITA, ECONOMIA
}
