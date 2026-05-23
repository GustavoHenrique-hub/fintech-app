package com.enterprise.gustadev.fintech_app.domain.categoria.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoriaThreshold {

    private UUID id;
    private UUID categoriaId;
    private short thresholdAuto;
    private short thresholdAlerta;
    private boolean ambiguidadeAlta;

    public CategoriaThreshold(UUID id, UUID categoriaId, short thresholdAuto,
                               short thresholdAlerta, boolean ambiguidadeAlta) {
        this.id = id;
        this.categoriaId = categoriaId;
        this.thresholdAuto = thresholdAuto;
        this.thresholdAlerta = thresholdAlerta;
        this.ambiguidadeAlta = ambiguidadeAlta;
    }

    public CategoriaThreshold(UUID categoriaId, short thresholdAuto, short thresholdAlerta, boolean ambiguidadeAlta) {
        this(null, categoriaId, thresholdAuto, thresholdAlerta, ambiguidadeAlta);
    }
}
