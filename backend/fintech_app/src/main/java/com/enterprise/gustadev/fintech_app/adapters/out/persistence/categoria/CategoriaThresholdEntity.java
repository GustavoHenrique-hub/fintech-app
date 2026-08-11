package com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoria;

import com.enterprise.gustadev.fintech_app.domain.categoria.model.CategoriaThreshold;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "categoria_thresholds")
@Getter
@Setter
@NoArgsConstructor
public class CategoriaThresholdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_threshold_id")
    private Long id;

    @Column(name = "categoria_threshold_code", unique = true, nullable = false, length = 6)
    private String code;

    @Column(name = "categoria_id", nullable = false, unique = true)
    private Long categoriaId;

    @Column(name = "categoria_code", nullable = false, length = 6)
    private String categoriaCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
            value = {
                    @JoinColumn(name = "categoria_id", referencedColumnName = "categoria_id",
                            insertable = false, updatable = false),
                    @JoinColumn(name = "categoria_code", referencedColumnName = "categoria_code",
                            insertable = false, updatable = false)
            },
            foreignKey = @ForeignKey(name = "fk_categoria_thresholds_categoria")
    )
    private CategoriaEntity categoria;

    @Column(name = "threshold_auto", nullable = false)
    private short thresholdAuto = 70;

    @Column(name = "threshold_alerta", nullable = false)
    private short thresholdAlerta = 50;

    @Column(name = "ambiguidade_alta", nullable = false)
    private boolean ambiguidadeAlta = false;

    public static CategoriaThresholdEntity fromDomain(CategoriaThreshold domain) {
        CategoriaThresholdEntity entity = new CategoriaThresholdEntity();
        entity.id = domain.getId();
        entity.code = domain.getCode();
        entity.categoriaId = domain.getCategoriaId();
        entity.categoriaCode = domain.getCategoriaCode();
        entity.thresholdAuto = domain.getThresholdAuto();
        entity.thresholdAlerta = domain.getThresholdAlerta();
        entity.ambiguidadeAlta = domain.isAmbiguidadeAlta();
        return entity;
    }

    public CategoriaThreshold toDomain() {
        CategoriaThreshold t = new CategoriaThreshold(id, categoriaId, categoriaCode,
                thresholdAuto, thresholdAlerta, ambiguidadeAlta);
        t.setCode(code);
        return t;
    }
}
