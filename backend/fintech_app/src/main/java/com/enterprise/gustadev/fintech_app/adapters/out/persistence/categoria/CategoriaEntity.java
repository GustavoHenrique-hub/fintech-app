package com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoria;

import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
public class CategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "categoria_pai_id")
    private UUID categoriaPaiId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoCategoria tipo;

    @Column(length = 50)
    private String icone;

    @Column(name = "cor_hex", length = 7)
    private String corHex;

    @Column
    private Boolean padrao = false;

    @Column(name = "usuario_id")
    private UUID usuarioId;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    public static CategoriaEntity fromDomain(Categoria domain) {
        CategoriaEntity entity = new CategoriaEntity();
        entity.id = domain.getId();
        entity.nome = domain.getNome();
        entity.categoriaPaiId = domain.getCategoriaPaiId();
        entity.tipo = domain.getTipo();
        entity.icone = domain.getIcone();
        entity.corHex = domain.getCorHex();
        entity.padrao = domain.isPadrao();
        entity.usuarioId = domain.getUsuarioId();
        entity.criadoEm = domain.getCriadoEm();
        return entity;
    }

    public Categoria toDomain() {
        return new Categoria(id, nome, categoriaPaiId, tipo, icone, corHex,
                padrao != null && padrao, usuarioId, criadoEm);
    }
}
