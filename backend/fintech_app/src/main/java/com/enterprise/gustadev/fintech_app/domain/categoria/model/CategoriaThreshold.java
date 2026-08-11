package com.enterprise.gustadev.fintech_app.domain.categoria.model;

import com.enterprise.gustadev.fintech_app.domain.shared.util.CodeGenerator;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoriaThreshold {

    private Long id;
    private String code;
    private Long categoriaId;
    private String categoriaCode;
    private short thresholdAuto;
    private short thresholdAlerta;
    private boolean ambiguidadeAlta;

    public CategoriaThreshold(Long id, Long categoriaId, String categoriaCode, short thresholdAuto,
                               short thresholdAlerta, boolean ambiguidadeAlta) {
        this.id = id;
        this.categoriaId = categoriaId;
        this.categoriaCode = categoriaCode;
        this.thresholdAuto = thresholdAuto;
        this.thresholdAlerta = thresholdAlerta;
        this.ambiguidadeAlta = ambiguidadeAlta;
    }

    public CategoriaThreshold(Long categoriaId, String categoriaCode, short thresholdAuto,
                               short thresholdAlerta, boolean ambiguidadeAlta) {
        this(null, categoriaId, categoriaCode, thresholdAuto, thresholdAlerta, ambiguidadeAlta);
        this.code = CodeGenerator.gerar();
    }
}
