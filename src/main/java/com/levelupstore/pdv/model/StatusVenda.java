package com.levelupstore.pdv.model;

import jakarta.persistence.*;

@Entity
@Table(name = "status_venda")
public class StatusVenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao; // Ex: AGUARDANDO_PAGAMENTO, FINALIZADA

    public StatusVenda() {}
    public StatusVenda(String descricao) {
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}