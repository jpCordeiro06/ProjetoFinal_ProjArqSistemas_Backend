package com.levelupstore.estoque.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "colecionaveis")
@PrimaryKeyJoinColumn(name = "produto_id")
public class Colecionavel extends Produto {

    private String linha;
    private String fabricante;

    public Colecionavel() {
        super();
    }


    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        if (linha == null || linha.trim().isEmpty()) {
            throw new IllegalArgumentException("A linha do colecionável é obrigatória.");
        }
        this.linha = linha;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        if (fabricante == null || fabricante.trim().isEmpty()) {
            throw new IllegalArgumentException("O fabricante é obrigatório.");
        }
        this.fabricante = fabricante;
    }
}