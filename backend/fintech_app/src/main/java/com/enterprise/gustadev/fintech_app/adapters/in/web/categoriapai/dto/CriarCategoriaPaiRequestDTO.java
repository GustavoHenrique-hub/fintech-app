package com.enterprise.gustadev.fintech_app.adapters.in.web.categoriapai.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CriarCategoriaPaiRequestDTO(
        @NotNull UUID categoriaId,
        @NotNull UUID paiId
) {}
