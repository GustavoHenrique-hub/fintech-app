package com.enterprise.gustadev.fintech_app.domain.extrato.port;

public interface ArmazenamentoArquivoPort {
    /** Persiste o conteúdo bruto do extrato enviado, identificado por {@code arquivoUuid}. */
    void salvar(String arquivoUuid, String nomeOriginal, byte[] conteudo);

    /** Lê de volta o conteúdo gravado por {@link #salvar} — usado no reenvio do extrato. */
    byte[] carregar(String arquivoUuid, String nomeOriginal);
}
