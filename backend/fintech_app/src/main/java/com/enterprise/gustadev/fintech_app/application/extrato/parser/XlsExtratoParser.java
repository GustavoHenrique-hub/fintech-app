package com.enterprise.gustadev.fintech_app.application.extrato.parser;

import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.FormatoExtrato;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Lê a primeira planilha do arquivo, esperando colunas [data, ...descrição..., valor]. */
@Component
public class XlsExtratoParser implements ExtratoParser {

    @Override
    public boolean suporta(FormatoExtrato formato) {
        return formato == FormatoExtrato.XLS || formato == FormatoExtrato.XLSX;
    }

    @Override
    public List<LancamentoExtraido> parsear(byte[] conteudo) {
        List<LancamentoExtraido> lancamentos = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(conteudo))) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                LancamentoExtraido lancamento = interpretar(row);
                if (lancamento != null) lancamentos.add(lancamento);
            }
        } catch (IOException | RuntimeException e) {
            throw new ExtratoInvalidoException("Não foi possível ler a planilha: " + e.getMessage());
        }

        if (lancamentos.isEmpty()) {
            throw new ExtratoInvalidoException("Nenhum lançamento reconhecido no arquivo");
        }
        return lancamentos;
    }

    private LancamentoExtraido interpretar(Row row) {
        int ultimaColuna = row.getLastCellNum() - 1;
        if (ultimaColuna < 1) return null;

        Optional<LocalDate> data = lerData(row.getCell(0));
        if (data.isEmpty()) return null;

        Optional<BigDecimal> valor = lerValor(row.getCell(ultimaColuna));
        if (valor.isEmpty()) return null;

        StringBuilder descricao = new StringBuilder();
        for (int i = 1; i < ultimaColuna; i++) {
            String texto = lerTexto(row.getCell(i));
            if (texto.isBlank()) continue;
            if (!descricao.isEmpty()) descricao.append(" ");
            descricao.append(texto);
        }
        return new LancamentoExtraido(data.get(), descricao.toString().trim(), valor.get());
    }

    private Optional<LocalDate> lerData(Cell cell) {
        if (cell == null) return Optional.empty();
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return Optional.of(cell.getLocalDateTimeCellValue().toLocalDate());
        }
        return ExtratoParsingUtils.parseData(lerTexto(cell));
    }

    private Optional<BigDecimal> lerValor(Cell cell) {
        if (cell == null) return Optional.empty();
        if (cell.getCellType() == CellType.NUMERIC) {
            return Optional.of(BigDecimal.valueOf(cell.getNumericCellValue()));
        }
        return ExtratoParsingUtils.parseValor(lerTexto(cell));
    }

    private String lerTexto(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
}
