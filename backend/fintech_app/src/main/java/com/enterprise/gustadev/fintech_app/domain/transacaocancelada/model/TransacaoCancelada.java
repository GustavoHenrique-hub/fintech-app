package com.enterprise.gustadev.fintech_app.domain.transacaocancelada.model;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.CanceladoPor;
import com.enterprise.gustadev.fintech_app.domain.shared.util.CodeGenerator;
import com.enterprise.gustadev.fintech_app.domain.transacaocancelada.exception.TransacaoCanceladaInvalidaException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class TransacaoCancelada {

    private Long id;
    private String code;
    private Long transacaoId;
    private String transacaoCode;
    private Long motivoId;
    private String motivoCode;
    private CanceladoPor canceladoPor;
    private BigDecimal valorOriginal;
    private String observacao;
    private String ipOrigem;
    private OffsetDateTime canceladoEm;

    public TransacaoCancelada(Long id, Long transacaoId, String transacaoCode, Long motivoId, String motivoCode,
                               CanceladoPor canceladoPor, BigDecimal valorOriginal,
                               String observacao, String ipOrigem, OffsetDateTime canceladoEm) {
        this.id = id;
        this.transacaoId = transacaoId;
        this.transacaoCode = transacaoCode;
        this.motivoId = motivoId;
        this.motivoCode = motivoCode;
        this.canceladoPor = canceladoPor;
        this.valorOriginal = valorOriginal;
        this.observacao = observacao;
        this.ipOrigem = ipOrigem;
        this.canceladoEm = canceladoEm;
    }

    public TransacaoCancelada(Long transacaoId, String transacaoCode, Long motivoId, String motivoCode,
                               CanceladoPor canceladoPor, BigDecimal valorOriginal, String observacao, String ipOrigem) {
        this(null, transacaoId, transacaoCode, motivoId, motivoCode, canceladoPor,
                valorOriginal, observacao, ipOrigem, null);
        this.code = CodeGenerator.gerar();
    }

    public void validar() {
        if (transacaoId == null) {
            throw new TransacaoCanceladaInvalidaException("transacaoId é obrigatório");
        }
        if (motivoId == null) {
            throw new TransacaoCanceladaInvalidaException("motivoId é obrigatório");
        }
        if (valorOriginal == null || valorOriginal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransacaoCanceladaInvalidaException("valorOriginal deve ser positivo");
        }
    }
}
