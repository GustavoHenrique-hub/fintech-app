package com.enterprise.gustadev.fintech_app.adapters.in.web.banco.dto;

import com.enterprise.gustadev.fintech_app.domain.banco.model.Banco;

public record BancoResponseDTO(
        Long id,
        String code,
        String nome,
        String descricao,
        String corHex,
        String icone
) {
    public static BancoResponseDTO fromDomain(Banco domain) {
        return new BancoResponseDTO(
                domain.getId(),
                domain.getCode(),
                domain.getNome(),
                domain.getDescricao(),
                domain.getCorHex(),
                domain.getIcone()
        );
    }
}
