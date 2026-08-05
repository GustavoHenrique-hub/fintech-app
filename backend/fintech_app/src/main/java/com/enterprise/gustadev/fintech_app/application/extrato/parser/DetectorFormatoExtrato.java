package com.enterprise.gustadev.fintech_app.application.extrato.parser;

import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.FormatoExtrato;

public final class DetectorFormatoExtrato {

    private DetectorFormatoExtrato() {}

    public static FormatoExtrato detectar(String nomeArquivo) {
        String extensao = extensao(nomeArquivo);
        return switch (extensao) {
            case "pdf" -> FormatoExtrato.PDF;
            case "csv" -> FormatoExtrato.CSV;
            case "txt" -> FormatoExtrato.TXT;
            case "xls" -> FormatoExtrato.XLS;
            case "xlsx" -> FormatoExtrato.XLSX;
            default -> throw new ExtratoInvalidoException(
                    "Formato de arquivo não suportado: ." + extensao
                            + " (aceitos: pdf, csv, txt, xls, xlsx)");
        };
    }

    private static String extensao(String nomeArquivo) {
        if (nomeArquivo == null) throw new ExtratoInvalidoException("Nome do arquivo é obrigatório");
        int ponto = nomeArquivo.lastIndexOf('.');
        if (ponto < 0 || ponto == nomeArquivo.length() - 1) {
            throw new ExtratoInvalidoException("Arquivo sem extensão reconhecível");
        }
        return nomeArquivo.substring(ponto + 1).toLowerCase();
    }
}
