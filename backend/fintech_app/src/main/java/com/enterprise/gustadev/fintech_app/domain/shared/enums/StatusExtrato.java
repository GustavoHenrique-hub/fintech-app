package com.enterprise.gustadev.fintech_app.domain.shared.enums;

public enum StatusExtrato {
    upload_recebido, validando, na_fila, extraindo, classificando,
    aguardando_ia, pendente_revisao, parcialmente_revisado, concluido,
    erro_formato, erro_extracao, erro_classificacao, erro_timeout,
    cancelado, reprocessando
}
