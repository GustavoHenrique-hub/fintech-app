package com.enterprise.gustadev.fintech_app.adapters.out.storage.arquivo;

import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ArmazenamentoArquivoPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Grava o arquivo em disco local sob {@code app.upload.dir}, nomeado pelo UUID gerado
 * no upload (nunca o nome original — evita colisão e vazamento de path/nome do usuário).
 */
@Component
public class ArmazenamentoArquivoLocalAdapter implements ArmazenamentoArquivoPort {

    private final Path diretorio;

    public ArmazenamentoArquivoLocalAdapter(@Value("${app.upload.dir:./uploads/extratos}") String diretorioConfigurado) {
        this.diretorio = Path.of(diretorioConfigurado);
    }

    @Override
    public void salvar(String arquivoUuid, String nomeOriginal, byte[] conteudo) {
        try {
            Files.createDirectories(diretorio);
            Path destino = diretorio.resolve(arquivoUuid + extensao(nomeOriginal));
            Files.write(destino, conteudo, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new ExtratoInvalidoException("Não foi possível armazenar o arquivo: " + e.getMessage());
        }
    }

    private String extensao(String nomeOriginal) {
        if (nomeOriginal == null) return "";
        int ponto = nomeOriginal.lastIndexOf('.');
        return ponto >= 0 ? nomeOriginal.substring(ponto) : "";
    }
}
