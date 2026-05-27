package com.enterprise.gustadev.fintech_app.adapters.out.persistence.banco;

import com.enterprise.gustadev.fintech_app.domain.banco.model.Banco;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "banco")
@Getter
@Setter
@NoArgsConstructor
public class BancoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banco_id")
    private Long id;

    @Column(name = "banco_code", unique = true, nullable = false, length = 6)
    private String code;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(name = "cor_hex", length = 7)
    private String corHex;

    @Column(length = 50)
    private String icone;

    public static BancoEntity fromDomain(Banco domain) {
        BancoEntity entity = new BancoEntity();
        entity.id = domain.getId();
        entity.code = domain.getCode();
        entity.nome = domain.getNome();
        entity.descricao = domain.getDescricao();
        entity.corHex = domain.getCorHex();
        entity.icone = domain.getIcone();
        return entity;
    }

    public Banco toDomain() {
        return new Banco(id, code, nome, descricao, corHex, icone);
    }
}
