package com.enterprise.gustadev.fintech_app.domain.categoria.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoriaThreshold {

    private Long id;
    private Long categoriaId;
    private short thresholdAuto;
    private short thresholdAlerta;
    private boolean ambiguidadeAlta;

    public CategoriaThreshold(Long id, Long categoriaId, short thresholdAuto,
                               short thresholdAlerta, boolean ambiguidadeAlta) {
        this.id = id;
        this.categoriaId = categoriaId;
        this.thresholdAuto = thresholdAuto;
        this.thresholdAlerta = thresholdAlerta;
        this.ambiguidadeAlta = ambiguidadeAlta;
    }

    public CategoriaThreshold(Long categoriaId, short thresholdAuto, short thresholdAlerta, boolean ambiguidadeAlta) {
        this(null, categoriaId, thresholdAuto, thresholdAlerta, ambiguidadeAlta);
    }
}
