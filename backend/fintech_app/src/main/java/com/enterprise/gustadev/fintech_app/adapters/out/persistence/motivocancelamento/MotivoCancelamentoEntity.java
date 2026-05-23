package com.enterprise.gustadev.fintech_app.adapters.out.persistence.motivocancelamento;

import com.enterprise.gustadev.fintech_app.domain.motivocancelamento.model.MotivoCancelamento;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemPermitidaCancelamento;
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

@Entity
@Table(name = "motivos_cancelamento")
@Getter
@Setter
@NoArgsConstructor
public class MotivoCancelamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "motivos_cancelamento_code", unique = true, nullable = false, length = 6)
    private String code;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "origem_permitida", nullable = false, length = 20)
    private OrigemPermitidaCancelamento origemPermitida = OrigemPermitidaCancelamento.todos;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    public static MotivoCancelamentoEntity fromDomain(MotivoCancelamento domain) {
        MotivoCancelamentoEntity entity = new MotivoCancelamentoEntity();
        entity.id = domain.getId();
        entity.code = domain.getCode();
        entity.descricao = domain.getDescricao();
        entity.origemPermitida = domain.getOrigemPermitida();
        entity.ativo = domain.isAtivo();
        entity.criadoEm = domain.getCriadoEm();
        return entity;
    }

    public MotivoCancelamento toDomain() {
        MotivoCancelamento m = new MotivoCancelamento(id, descricao, origemPermitida, ativo, criadoEm);
        m.setCode(code);
        return m;
    }
}
