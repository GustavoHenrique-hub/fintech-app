package com.enterprise.gustadev.fintech_app.domain.extrato.port;

public interface ArmazenamentoArquivoPort {
    /** Persiste o conteúdo bruto do extrato enviado, identificado por {@code arquivoUuid}. */
    void salvar(String arquivoUuid, String nomeOriginal, byte[] conteudo);
}
