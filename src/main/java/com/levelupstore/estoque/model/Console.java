package com.levelupstore.estoque.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "consoles")
@PrimaryKeyJoinColumn(name = "produto_id")
public class Console extends Produto {

    private String marca;
    private String modelo;

    public Console() {
        super();
    }


    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("A marca do console é obrigatória.");
        }
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("O modelo do console é obrigatório.");
        }
        this.modelo = modelo;
    }
}