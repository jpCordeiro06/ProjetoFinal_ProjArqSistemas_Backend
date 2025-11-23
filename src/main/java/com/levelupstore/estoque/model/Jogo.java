package com.levelupstore.estoque.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

// Classe Jogo herda de Produto
@Entity
@Table(name = "jogos")
@PrimaryKeyJoinColumn(name = "produto_id")
public class Jogo extends Produto {

    private String plataforma;

    public Jogo() {
        super();
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        if (plataforma == null || plataforma.trim().isEmpty()) {
            throw new IllegalArgumentException("A plataforma do jogo é obrigatória.");
        }
        this.plataforma = plataforma;
    }
}