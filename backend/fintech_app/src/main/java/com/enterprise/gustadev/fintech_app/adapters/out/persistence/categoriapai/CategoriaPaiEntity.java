package com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoriapai;

import com.enterprise.gustadev.fintech_app.domain.categoriapai.model.CategoriaPai;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "categorias_pai")
@Getter
@Setter
@NoArgsConstructor
public class CategoriaPaiEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categoria_id", nullable = false)
    private Long categoriaId;

    @Column(name = "pai_id", nullable = false)
    private Long paiId;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    public static CategoriaPaiEntity fromDomain(CategoriaPai domain) {
        CategoriaPaiEntity entity = new CategoriaPaiEntity();
        entity.id = domain.getId();
        entity.categoriaId = domain.getCategoriaId();
        entity.paiId = domain.getPaiId();
        entity.criadoEm = domain.getCriadoEm();
        return entity;
    }

    public CategoriaPai toDomain() {
        return new CategoriaPai(id, categoriaId, paiId, criadoEm);
    }
}
