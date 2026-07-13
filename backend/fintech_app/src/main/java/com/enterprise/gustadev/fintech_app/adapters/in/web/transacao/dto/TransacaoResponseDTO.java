package com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto;

import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record TransacaoResponseDTO(
        Long id,
        String code,
        Long contaId,
        String contaCode,
        String indEstorno,
        String tipo,
        String descricao,
        BigDecimal valor,
        LocalDate dataTransacao,
        Long categoriaId,
        String estabelecimento,
        String origem,
        String statusRevisao,
        Short confiancaIa,
        boolean recorrente,
        OffsetDateTime criadoEm,
        OffsetDateTime estornadoAt
) {
    public static TransacaoResponseDTO fromDomain(Transacao domain) {
        return new TransacaoResponseDTO(
                domain.getId(),
                domain.getCode(),
                domain.getConta().getId(),
                domain.getConta().getCode(),
                domain.getIndEstorno(),
                domain.tipoEfetivo().name(),
                domain.getDescricao(),
                domain.getValor(),
                domain.getDataTransacao(),
                domain.getCategoriaId(),
                domain.getEstabelecimento(),
                domain.getOrigem().name(),
                domain.getStatusRevisao().name(),
                domain.getConfiancaIa(),
                domain.isRecorrente(),
                domain.getCriadoEm(),
                domain.getEstornadoAt()
        );
    }
}
