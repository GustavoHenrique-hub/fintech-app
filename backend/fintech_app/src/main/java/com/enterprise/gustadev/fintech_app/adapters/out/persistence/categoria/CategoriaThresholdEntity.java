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
    private Long id;

    @Column(name = "categoria_id", nullable = false, unique = true)
    private Long categoriaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_categoria_thresholds_categoria"))
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
        entity.categoriaId = domain.getCategoriaId();
        entity.thresholdAuto = domain.getThresholdAuto();
        entity.thresholdAlerta = domain.getThresholdAlerta();
        entity.ambiguidadeAlta = domain.isAmbiguidadeAlta();
        return entity;
    }

    public CategoriaThreshold toDomain() {
        return new CategoriaThreshold(id, categoriaId, thresholdAuto, thresholdAlerta, ambiguidadeAlta);
    }
}
