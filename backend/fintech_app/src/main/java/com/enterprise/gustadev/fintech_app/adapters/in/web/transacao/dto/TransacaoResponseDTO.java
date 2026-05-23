package com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto;

import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TransacaoResponseDTO(
        UUID id,
        UUID usuarioId,
        UUID contaId,
        UUID extratoId,
        String tipo,
        String descricaoUsuario,
        String descricaoNormalizada,
        BigDecimal valor,
        LocalDate dataTransacao,
        UUID categoriaId,
        String subcategoria,
        String estabelecimento,
        String origem,
        String statusRevisao,
        Short confiancaIa,
        boolean recorrente,
        OffsetDateTime criadoEm
) {
    public static TransacaoResponseDTO fromDomain(Transacao domain) {
        return new TransacaoResponseDTO(
                domain.getId(),
                domain.getUsuarioId(),
                domain.getContaId(),
                domain.getExtratoId(),
                domain.getTipo().name(),
                domain.getDescricaoUsuario(),
                domain.getDescricaoNormalizada(),
                domain.getValor(),
                domain.getDataTransacao(),
                domain.getCategoriaId(),
                domain.getSubcategoria(),
                domain.getEstabelecimento(),
                domain.getOrigem().name(),
                domain.getStatusRevisao().name(),
                domain.getConfiancaIa(),
                domain.isRecorrente(),
                domain.getCriadoEm()
        );
    }
}
