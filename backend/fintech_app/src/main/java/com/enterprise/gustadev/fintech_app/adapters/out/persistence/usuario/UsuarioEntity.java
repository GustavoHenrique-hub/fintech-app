package com.enterprise.gustadev.fintech_app.adapters.out.persistence.usuario;

import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.SecureRandom;
import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class UsuarioEntity {

    private static final SecureRandom RANDOM = new SecureRandom();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long idUsuario;

    @Column(name = "usuario_code", nullable = false, unique = true, length = 6)
    private String usuarioCode;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column
    private String telefone;

    @Column
    private Long telegramChatID;

    @Column
    private Long whatsappChatID;

    @Column
    private Boolean emailVerificado;

    @Column(nullable = false)
    private LocalDate dtNascimento;

    public static UsuarioEntity fromDomain(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.idUsuario = usuario.getIdUsuario();
        entity.usuarioCode = usuario.getUsuarioCode();
        entity.cpf = usuario.getCpf();
        entity.nome = usuario.getNome();
        entity.email = usuario.getEmail();
        entity.senha = usuario.getSenha();
        entity.telefone = usuario.getTelefone();
        entity.telegramChatID = usuario.getTelegramChatID();
        entity.whatsappChatID = usuario.getWhatsappChatID();
        entity.emailVerificado = usuario.getEmailVerificado();
        entity.dtNascimento = usuario.getDtNascimento();
        return entity;
    }

    public Usuario toDomain() {
        return new Usuario(idUsuario, usuarioCode, cpf, nome, email, senha, telefone, telegramChatID, whatsappChatID, emailVerificado, dtNascimento);
    }

    @PrePersist
    public void prePersist() {
        if(this.nome != null) {
            this.nome = nome.toUpperCase();
        }
        if (usuarioCode == null || usuarioCode.isBlank()) {
            usuarioCode = String.format("%05d", RANDOM.nextInt(100000));
        }
    }

    @PreUpdate
    public void preUpdate() {
        if(this.nome != null) {
            this.nome = nome.toUpperCase();
        }
    }
}
