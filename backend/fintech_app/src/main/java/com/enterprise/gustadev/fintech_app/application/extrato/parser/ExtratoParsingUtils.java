package com.enterprise.gustadev.fintech_app.application.extrato.parser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Heurísticas de parsing compartilhadas pelos parsers de extrato (CSV/TXT/XLS/PDF).
 * Extratos bancários não seguem um layout único, então aqui é "melhor esforço":
 * tenta os formatos de data e valor mais comuns em exports de bancos brasileiros.
 */
final class ExtratoParsingUtils {

    private static final List<DateTimeFormatter> FORMATOS_DATA = List.of(
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    );

    private static final Pattern VALOR_LIMPEZA = Pattern.compile("[Rr]\\$|\\s");
    private static final Pattern SUFIXO_DC = Pattern.compile("(?i)\\s*([DC])$");

    private ExtratoParsingUtils() {}

    static Optional<LocalDate> parseData(String bruto) {
        if (bruto == null || bruto.isBlank()) return Optional.empty();
        String valor = bruto.trim();
        for (DateTimeFormatter formatter : FORMATOS_DATA) {
            try {
                return Optional.of(LocalDate.parse(valor, formatter));
            } catch (Exception ignored) {
                // tenta o próximo formato
            }
        }
        return Optional.empty();
    }

    /** Sinal já resolvido: negativo = gasto, positivo = receita. Sufixo D/C, quando presente, decide o sinal. */
    static Optional<BigDecimal> parseValor(String bruto) {
        if (bruto == null || bruto.isBlank()) return Optional.empty();
        String valor = VALOR_LIMPEZA.matcher(bruto.trim()).replaceAll("");
        if (valor.isBlank()) return Optional.empty();

        Boolean debito = null;
        Matcher sufixo = SUFIXO_DC.matcher(valor);
        if (sufixo.find()) {
            debito = "D".equalsIgnoreCase(sufixo.group(1));
            valor = valor.substring(0, sufixo.start());
        }

        boolean negativo = valor.startsWith("-");
        if (negativo || valor.startsWith("+")) valor = valor.substring(1);

        // Formato BR (1.234,56) vs formato US (1234.56): vírgula decimal só é
        // assumida quando aparece depois do último ponto (ou não há ponto).
        int ultimaVirgula = valor.lastIndexOf(',');
        int ultimoPonto = valor.lastIndexOf('.');
        if (ultimaVirgula > ultimoPonto) {
            valor = valor.replace(".", "").replace(",", ".");
        } else if (ultimoPonto > ultimaVirgula && ultimaVirgula >= 0) {
            valor = valor.replace(",", "");
        }

        try {
            BigDecimal decimal = new BigDecimal(valor);
            if (debito != null) {
                decimal = debito ? decimal.abs().negate() : decimal.abs();
            } else if (negativo) {
                decimal = decimal.abs().negate();
            }
            return Optional.of(decimal);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
