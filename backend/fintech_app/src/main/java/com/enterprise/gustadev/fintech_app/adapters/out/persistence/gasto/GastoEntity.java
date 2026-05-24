package com.enterprise.gustadev.fintech_app.adapters.out.persistence.gasto;

import com.enterprise.gustadev.fintech_app.adapters.out.persistence.usuario.UsuarioEntity;
import com.enterprise.gustadev.fintech_app.domain.gasto.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.gasto.model.Gasto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "gastos")
@Getter
@Setter
@NoArgsConstructor
public class GastoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_gastos_usuario"))
    private UsuarioEntity usuario;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private LocalDate data;

    public static GastoEntity fromDomain(Gasto gasto) {
        GastoEntity entity = new GastoEntity();
        entity.id = gasto.getId();
        entity.usuarioId = gasto.getUsuarioId();
        entity.valor = gasto.getValor();
        entity.descricao = gasto.getDescricao();
        entity.categoria = gasto.getCategoria();
        entity.data = gasto.getData();
        return entity;
    }

    public Gasto toDomain() {
        return new Gasto(id, usuarioId, valor, descricao, categoria, data);
    }
}