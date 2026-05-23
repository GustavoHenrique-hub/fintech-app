package com.enterprise.gustadev.fintech_app.adapters.out.persistence.categoriadousuario;

import com.enterprise.gustadev.fintech_app.domain.categoriadousuario.model.CategoriaDoUsuario;
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
import java.util.UUID;

@Entity
@Table(name = "categorias_do_usuario")
@Getter
@Setter
@NoArgsConstructor
public class CategoriaDoUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "categoria_id", nullable = false)
    private UUID categoriaId;

    @Column(nullable = false)
    private boolean ativa = true;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    public static CategoriaDoUsuarioEntity fromDomain(CategoriaDoUsuario domain) {
        CategoriaDoUsuarioEntity entity = new CategoriaDoUsuarioEntity();
        entity.id = domain.getId();
        entity.usuarioId = domain.getUsuarioId();
        entity.categoriaId = domain.getCategoriaId();
        entity.ativa = domain.isAtiva();
        entity.criadoEm = domain.getCriadoEm();
        return entity;
    }

    public CategoriaDoUsuario toDomain() {
        return new CategoriaDoUsuario(id, usuarioId, categoriaId, ativa, criadoEm);
    }
}
