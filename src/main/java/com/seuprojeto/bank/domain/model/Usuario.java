package com.seuprojeto.bank.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Exigência JPA
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Column(nullable = false, length = 20)
    private String role;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    // Construtor voltado para o negócio
    public Usuario(String nome, String email, String senhaHash) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome é obrigatório");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email é obrigatório");
        
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.role = "USER"; // Padrão de segurança: todo usuário nasce com menor privilégio
        this.criadoEm = LocalDateTime.now();
    }

    // Comportamento do Domínio
    public void promoverParaAdmin() {
        this.role = "ADMIN";
    }
}