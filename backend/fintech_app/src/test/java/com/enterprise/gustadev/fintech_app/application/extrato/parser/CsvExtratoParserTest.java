package com.enterprise.gustadev.fintech_app.application.extrato.parser;

import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.FormatoExtrato;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CsvExtratoParserTest {

    private final CsvExtratoParser parser = new CsvExtratoParser();

    @Test
    void suporta_csvETxt_masNaoOutrosFormatos() {
        assertThat(parser.suporta(FormatoExtrato.CSV)).isTrue();
        assertThat(parser.suporta(FormatoExtrato.TXT)).isTrue();
        assertThat(parser.suporta(FormatoExtrato.PDF)).isFalse();
        assertThat(parser.suporta(FormatoExtrato.XLSX)).isFalse();
    }

    @Test
    void parsear_deveExtrairLancamentos_comDelimitadorPontoEVirgulaEValorBR() {
        String csv = "Data;Descricao;Valor\n"
                + "05/08/2026;Supermercado;-150,50\n"
                + "06/08/2026;Salario;5000,00\n";

        List<LancamentoExtraido> lancamentos = parser.parsear(csv.getBytes(StandardCharsets.UTF_8));

        assertThat(lancamentos).hasSize(2);
        assertThat(lancamentos.get(0).data()).isEqualTo(LocalDate.of(2026, 8, 5));
        assertThat(lancamentos.get(0).descricao()).isEqualTo("Supermercado");
        assertThat(lancamentos.get(0).valor()).isEqualByComparingTo(new BigDecimal("-150.50"));
        assertThat(lancamentos.get(1).valor()).isEqualByComparingTo(new BigDecimal("5000.00"));
    }

    @Test
    void parsear_deveExtrairLancamentos_comDelimitadorVirgulaESemCabecalho() {
        String csv = "01/08/2026,Uber,-23.90\n"
                + "02/08/2026,Freela,300.00\n";

        List<LancamentoExtraido> lancamentos = parser.parsear(csv.getBytes(StandardCharsets.UTF_8));

        assertThat(lancamentos).hasSize(2);
        assertThat(lancamentos.get(0).descricao()).isEqualTo("Uber");
        assertThat(lancamentos.get(0).valor()).isEqualByComparingTo(new BigDecimal("-23.90"));
    }

    @Test
    void parsear_deveResolverSinalPeloSufixoDC() {
        String csv = "05/08/2026;Compra no debito;150,00 D\n"
                + "06/08/2026;Deposito;200,00 C\n";

        List<LancamentoExtraido> lancamentos = parser.parsear(csv.getBytes(StandardCharsets.UTF_8));

        assertThat(lancamentos.get(0).valor()).isEqualByComparingTo(new BigDecimal("-150.00"));
        assertThat(lancamentos.get(1).valor()).isEqualByComparingTo(new BigDecimal("200.00"));
    }

    @Test
    void parsear_deveLancarExcecao_quandoNenhumaLinhaReconhecida() {
        String csv = "isto;nao;e um extrato\noutra;linha;invalida\n";

        assertThatThrownBy(() -> parser.parsear(csv.getBytes(StandardCharsets.UTF_8)))
                .isInstanceOf(ExtratoInvalidoException.class);
    }
}
