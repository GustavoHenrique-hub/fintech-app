package com.enterprise.gustadev.fintech_app.adapters.in.web.categoriapai.dto;

import jakarta.validation.constraints.NotNull;


public record CriarCategoriaPaiRequestDTO(
        @NotNull Long categoriaId,
        @NotNull Long paiId
) {}
