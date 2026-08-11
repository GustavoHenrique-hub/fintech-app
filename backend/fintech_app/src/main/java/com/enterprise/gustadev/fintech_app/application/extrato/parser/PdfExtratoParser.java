package com.enterprise.gustadev.fintech_app.application.extrato.parser;

import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.FormatoExtrato;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extrai texto do PDF e interpreta linha a linha. Extratos em PDF variam muito por
 * banco, então isto é "melhor esforço": casa uma data no início da linha e um valor
 * monetário no final; o texto entre os dois vira a descrição.
 */
@Component
public class PdfExtratoParser implements ExtratoParser {

    private static final Pattern LINHA_LANCAMENTO = Pattern.compile(
            "^\\s*(\\d{2}[/-]\\d{2}[/-]\\d{2,4})\\s+(.+?)\\s+([-+]?R?\\$?\\s?[\\d.,]+\\s?[DC]?)\\s*$");

    @Override
    public boolean suporta(FormatoExtrato formato) {
        return formato == FormatoExtrato.PDF;
    }

    @Override
    public List<LancamentoExtraido> parsear(byte[] conteudo) {
        String texto;
        try (PDDocument document = Loader.loadPDF(conteudo)) {
            texto = new PDFTextStripper().getText(document);
        } catch (IOException e) {
            throw new ExtratoInvalidoException("Não foi possível ler o PDF: " + e.getMessage());
        }

        List<LancamentoExtraido> lancamentos = new ArrayList<>();
        for (String linha : texto.split("\\R")) {
            Matcher matcher = LINHA_LANCAMENTO.matcher(linha);
            if (!matcher.matches()) continue;

            Optional<LocalDate> data = ExtratoParsingUtils.parseData(matcher.group(1));
            Optional<BigDecimal> valor = ExtratoParsingUtils.parseValor(matcher.group(3));
            if (data.isEmpty() || valor.isEmpty()) continue;

            lancamentos.add(new LancamentoExtraido(data.get(), matcher.group(2).trim(), valor.get()));
        }

        if (lancamentos.isEmpty()) {
            throw new ExtratoInvalidoException(
                    "Nenhum lançamento reconhecido no PDF — o layout deste banco ainda não é suportado");
        }
        return lancamentos;
    }
}
