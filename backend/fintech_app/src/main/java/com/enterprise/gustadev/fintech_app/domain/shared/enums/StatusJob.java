package com.enterprise.gustadev.fintech_app.domain.shared.enums;

public enum StatusJob {
    enfileirado, iniciando, processando, aguardando_ia,
    concluido, falha_ia, falha_parser, timeout,
    retry_1, retry_2, retry_3, dead_letter, cancelado
}
