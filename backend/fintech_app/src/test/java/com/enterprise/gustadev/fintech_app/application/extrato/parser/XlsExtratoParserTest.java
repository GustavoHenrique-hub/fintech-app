package com.enterprise.gustadev.fintech_app.application.extrato.parser;

import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.FormatoExtrato;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class XlsExtratoParserTest {

    private final XlsExtratoParser parser = new XlsExtratoParser();

    @Test
    void suporta_xlsExlsx_masNaoOutrosFormatos() {
        assertThat(parser.suporta(FormatoExtrato.XLS)).isTrue();
        assertThat(parser.suporta(FormatoExtrato.XLSX)).isTrue();
        assertThat(parser.suporta(FormatoExtrato.CSV)).isFalse();
    }

    @Test
    void parsear_deveExtrairLancamentos_deuPlanilhaComCabecalho() throws IOException {
        byte[] planilha = planilhaComLinhas(
                new Object[]{"Data", "Descricao", "Valor"},
                new Object[]{"05/08/2026", "Mercado", -150.5},
                new Object[]{"06/08/2026", "Salario", 5000.0}
        );

        List<LancamentoExtraido> lancamentos = parser.parsear(planilha);

        assertThat(lancamentos).hasSize(2);
        assertThat(lancamentos.get(0).data()).isEqualTo(LocalDate.of(2026, 8, 5));
        assertThat(lancamentos.get(0).descricao()).isEqualTo("Mercado");
        assertThat(lancamentos.get(0).valor()).isEqualByComparingTo(new BigDecimal("-150.5"));
        assertThat(lancamentos.get(1).valor()).isEqualByComparingTo(new BigDecimal("5000.0"));
    }

    @Test
    void parsear_deveLancarExcecao_quandoNenhumaLinhaReconhecida() {
        assertThatThrownBy(() -> parser.parsear(new byte[]{1, 2, 3}))
                .isInstanceOf(ExtratoInvalidoException.class);
    }

    private byte[] planilhaComLinhas(Object[]... linhas) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("extrato");
            for (int i = 0; i < linhas.length; i++) {
                Row row = sheet.createRow(i);
                Object[] valores = linhas[i];
                for (int j = 0; j < valores.length; j++) {
                    if (valores[j] instanceof Number numero) {
                        row.createCell(j).setCellValue(numero.doubleValue());
                    } else {
                        row.createCell(j).setCellValue(String.valueOf(valores[j]));
                    }
                }
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }
}
