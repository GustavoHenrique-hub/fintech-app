package com.enterprise.gustadev.fintech_app.application.extrato.parser;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.FormatoExtrato;

import java.util.List;

public interface ExtratoParser {

    boolean suporta(FormatoExtrato formato);

    /** @throws com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException se o conteúdo não puder ser lido */
    List<LancamentoExtraido> parsear(byte[] conteudo);
}
