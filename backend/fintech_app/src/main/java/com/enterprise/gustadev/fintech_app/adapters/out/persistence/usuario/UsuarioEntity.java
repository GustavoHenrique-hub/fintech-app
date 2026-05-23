package com.enterprise.gustadev.fintech_app.adapters.out.persistence.usuario;

import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String usercode;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String rg;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String sobrenome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    public static UsuarioEntity fromDomain(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.id = usuario.getId();
        entity.usercode = usuario.getUsercode();
        entity.cpf = usuario.getCpf();
        entity.rg = usuario.getRg();
        entity.nome = usuario.getNome();
        entity.sobrenome = usuario.getSobrenome();
        entity.email = usuario.getEmail();
        entity.senha = usuario.getSenha();
        entity.dataNascimento = usuario.getDataNascimento();
        return entity;
    }

    public Usuario toDomain() {
        return new Usuario(id, usercode, cpf, rg, nome, sobrenome, email, senha, dataNascimento);
    }
}
