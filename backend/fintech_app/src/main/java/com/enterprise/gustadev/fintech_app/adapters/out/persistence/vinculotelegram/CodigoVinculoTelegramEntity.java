package com.enterprise.gustadev.fintech_app.adapters.out.persistence.vinculotelegram;

import com.enterprise.gustadev.fintech_app.domain.usuario.model.CodigoVinculoTelegram;
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
@Table(name = "codigos_vinculo_telegram")
@Getter
@Setter
@NoArgsConstructor
public class CodigoVinculoTelegramEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_vinculo_telegram_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    @Column(name = "expira_em", nullable = false)
    private OffsetDateTime expiraEm;

    @Column(name = "usado_em")
    private OffsetDateTime usadoEm;

    public static CodigoVinculoTelegramEntity fromDomain(CodigoVinculoTelegram domain) {
        CodigoVinculoTelegramEntity entity = new CodigoVinculoTelegramEntity();
        entity.id = domain.getId();
        entity.codigo = domain.getCodigo();
        entity.usuarioId = domain.getUsuarioId();
        entity.criadoEm = domain.getCriadoEm();
        entity.expiraEm = domain.getExpiraEm();
        entity.usadoEm = domain.getUsadoEm();
        return entity;
    }

    public CodigoVinculoTelegram toDomain() {
        return new CodigoVinculoTelegram(id, codigo, usuarioId, criadoEm, expiraEm, usadoEm);
    }
}
