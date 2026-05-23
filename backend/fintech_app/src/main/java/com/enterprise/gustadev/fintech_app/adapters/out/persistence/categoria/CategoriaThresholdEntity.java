package com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoria;

import com.enterprise.gustadev.fintech_app.domain.categoria.model.CategoriaThreshold;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "categoria_thresholds")
@Getter
@Setter
@NoArgsConstructor
public class CategoriaThresholdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "categoria_id", nullable = false, unique = true)
    private UUID categoriaId;

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
