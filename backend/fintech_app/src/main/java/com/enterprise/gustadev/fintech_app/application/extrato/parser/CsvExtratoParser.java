package com.enterprise.gustadev.fintech_app.application.extrato.parser;

import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.FormatoExtrato;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Extratos bancários em CSV/TXT não seguem um layout único, então o delimitador é
 * detectado pela linha mais frequente entre os candidatos comuns em exports BR.
 */
@Component
public class CsvExtratoParser implements ExtratoParser {

    private static final char[] DELIMITADORES_CANDIDATOS = {';', ',', '\t', '|'};

    @Override
    public boolean suporta(FormatoExtrato formato) {
        return formato == FormatoExtrato.CSV || formato == FormatoExtrato.TXT;
    }

    @Override
    public List<LancamentoExtraido> parsear(byte[] conteudo) {
        String texto = new String(conteudo, StandardCharsets.UTF_8);
        char delimitador = detectarDelimitador(texto);

        List<LancamentoExtraido> lancamentos = new ArrayList<>();
        try (CSVParser parser = CSVParser.parse(new StringReader(texto),
                CSVFormat.DEFAULT.builder().setDelimiter(delimitador).setTrim(true).build())) {
            boolean primeiraLinha = true;
            for (CSVRecord registro : parser) {
                if (registro.size() < 2) continue;
                LancamentoExtraido lancamento = interpretar(registro);
                if (lancamento == null) {
                    if (primeiraLinha) {
                        primeiraLinha = false;
                        continue; // provável cabeçalho
                    }
                    continue;
                }
                lancamentos.add(lancamento);
                primeiraLinha = false;
            }
        } catch (IOException e) {
            throw new ExtratoInvalidoException("Não foi possível ler o arquivo CSV/TXT: " + e.getMessage());
        }

        if (lancamentos.isEmpty()) {
            throw new ExtratoInvalidoException("Nenhum lançamento reconhecido no arquivo");
        }
        return lancamentos;
    }

    /** Colunas: [data, ...descrição..., valor] — a última coluna numérica é o valor. */
    private LancamentoExtraido interpretar(CSVRecord registro) {
        Optional<java.time.LocalDate> data = ExtratoParsingUtils.parseData(registro.get(0));
        if (data.isEmpty()) return null;

        int ultimaColuna = registro.size() - 1;
        Optional<java.math.BigDecimal> valor = ExtratoParsingUtils.parseValor(registro.get(ultimaColuna));
        if (valor.isEmpty()) return null;

        StringBuilder descricao = new StringBuilder();
        for (int i = 1; i < ultimaColuna; i++) {
            if (!descricao.isEmpty()) descricao.append(" ");
            descricao.append(registro.get(i));
        }
        return new LancamentoExtraido(data.get(), descricao.toString().trim(), valor.get());
    }

    private char detectarDelimitador(String texto) {
        String primeiraLinha = texto.lines().findFirst().orElse("");
        char melhor = DELIMITADORES_CANDIDATOS[0];
        long melhorContagem = -1;
        for (char candidato : DELIMITADORES_CANDIDATOS) {
            long contagem = primeiraLinha.chars().filter(c -> c == candidato).count();
            if (contagem > melhorContagem) {
                melhorContagem = contagem;
                melhor = candidato;
            }
        }
        return melhor;
    }
}
