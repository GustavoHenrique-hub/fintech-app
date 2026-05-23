package com.enterprise.gustadev.fintech_app.domain.transacaocancelada.model;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.CanceladoPor;
import com.enterprise.gustadev.fintech_app.domain.transacaocancelada.exception.TransacaoCanceladaInvalidaException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class TransacaoCancelada {

    private Long id;
    private Long transacaoId;
    private Long usuarioId;
    private Long contaId;
    private Long motivoId;
    private CanceladoPor canceladoPor;
    private BigDecimal valorOriginal;
    private String observacao;
    private String ipOrigem;
    private OffsetDateTime canceladoEm;

    public TransacaoCancelada(Long id, Long transacaoId, Long usuarioId, Long contaId,
                               Long motivoId, CanceladoPor canceladoPor, BigDecimal valorOriginal,
                               String observacao, String ipOrigem, OffsetDateTime canceladoEm) {
        this.id = id;
        this.transacaoId = transacaoId;
        this.usuarioId = usuarioId;
        this.contaId = contaId;
        this.motivoId = motivoId;
        this.canceladoPor = canceladoPor;
        this.valorOriginal = valorOriginal;
        this.observacao = observacao;
        this.ipOrigem = ipOrigem;
        this.canceladoEm = canceladoEm;
    }

    public TransacaoCancelada(Long transacaoId, Long usuarioId, Long contaId,
                               Long motivoId, CanceladoPor canceladoPor, BigDecimal valorOriginal,
                               String observacao, String ipOrigem) {
        this(null, transacaoId, usuarioId, contaId, motivoId, canceladoPor,
                valorOriginal, observacao, ipOrigem, null);
    }

    public void validar() {
        if (transacaoId == null) {
            throw new TransacaoCanceladaInvalidaException("transacaoId é obrigatório");
        }
        if (usuarioId == null) {
            throw new TransacaoCanceladaInvalidaException("usuarioId é obrigatório");
        }
        if (contaId == null) {
            throw new TransacaoCanceladaInvalidaException("contaId é obrigatório");
        }
        if (motivoId == null) {
            throw new TransacaoCanceladaInvalidaException("motivoId é obrigatório");
        }
        if (valorOriginal == null || valorOriginal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransacaoCanceladaInvalidaException("valorOriginal deve ser positivo");
        }
    }
}
