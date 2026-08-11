package com.enterprise.gustadev.fintech_app.domain.economia.model;

import com.enterprise.gustadev.fintech_app.domain.economia.exception.MovimentacaoEconomiaInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoMovimentacaoEconomia;
import com.enterprise.gustadev.fintech_app.domain.shared.util.CodeGenerator;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Ledger de auditoria de aportes/resgates do sub-saldo "Economias" de uma
 * {@link com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira}.
 *
 * <p>Não é receita nem gasto — é uma transferência interna entre o saldo
 * disponível e o sub-saldo de economias da MESMA conta, então NÃO usa a
 * entidade {@code Transacao} (que assume tipo RECEITA/GASTO por categoria)
 * e NÃO deve aparecer em {@code BuscarResumoPeriodoUseCase}.
 */
@Getter
@Setter
public class MovimentacaoEconomia {

    private Long id;
    private String code;
    private Long contaId;
    private String contaCode;
    private TipoMovimentacaoEconomia tipo;
    private BigDecimal valor;
    private String descricao;
    private OffsetDateTime dataMovimentacao;
    private OffsetDateTime criadoEm;

    public MovimentacaoEconomia(Long id, String code, Long contaId, String contaCode,
                                 TipoMovimentacaoEconomia tipo, BigDecimal valor,
                                 String descricao, OffsetDateTime dataMovimentacao,
                                 OffsetDateTime criadoEm) {
        this.id = id;
        this.code = code;
        this.contaId = contaId;
        this.contaCode = contaCode;
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.dataMovimentacao = dataMovimentacao;
        this.criadoEm = criadoEm;
    }

    /** Construtor de criação: gera code, dataMovimentacao e criadoEm. */
    public MovimentacaoEconomia(Long contaId, String contaCode, TipoMovimentacaoEconomia tipo,
                                 BigDecimal valor, String descricao) {
        this(null, CodeGenerator.gerar(), contaId, contaCode, tipo, valor, descricao,
                OffsetDateTime.now(), OffsetDateTime.now());
    }

    public void validar() {
        if (contaId == null) {
            throw new MovimentacaoEconomiaInvalidaException("contaId é obrigatório");
        }
        if (contaCode == null || contaCode.isBlank()) {
            throw new MovimentacaoEconomiaInvalidaException("contaCode é obrigatório");
        }
        if (tipo == null) {
            throw new MovimentacaoEconomiaInvalidaException("tipo é obrigatório");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MovimentacaoEconomiaInvalidaException("valor deve ser positivo");
        }
    }
}
