package com.levelupstore.estoque.model;

import jakarta.persistence.*;

@Entity
@Table(name = "condicoes")
public class Condicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    public Condicao() {
    }

    public Condicao(String nome) {
        this.nome = nome;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da condição não pode ser vazio.");
        }
        this.nome = nome;
    }
}