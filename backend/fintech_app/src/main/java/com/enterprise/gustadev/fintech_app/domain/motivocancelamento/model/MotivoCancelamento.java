package com.enterprise.gustadev.fintech_app.domain.motivocancelamento.model;

import com.enterprise.gustadev.fintech_app.domain.motivocancelamento.exception.MotivoCancelamentoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemPermitidaCancelamento;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class MotivoCancelamento {

    private Long id;
    private String code;
    private String descricao;
    private OrigemPermitidaCancelamento origemPermitida;
    private boolean ativo;
    private OffsetDateTime criadoEm;

    public MotivoCancelamento(Long id, String descricao, OrigemPermitidaCancelamento origemPermitida,
                               boolean ativo, OffsetDateTime criadoEm) {
        this.id = id;
        this.descricao = descricao;
        this.origemPermitida = origemPermitida;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
    }

    public MotivoCancelamento(String descricao, OrigemPermitidaCancelamento origemPermitida) {
        this(null, descricao, origemPermitida, true, null);
    }

    public void validar() {
        if (descricao == null || descricao.isBlank()) {
            throw new MotivoCancelamentoInvalidoException("Descrição é obrigatória");
        }
        if (origemPermitida == null) {
            throw new MotivoCancelamentoInvalidoException("origemPermitida é obrigatória");
        }
    }
}
