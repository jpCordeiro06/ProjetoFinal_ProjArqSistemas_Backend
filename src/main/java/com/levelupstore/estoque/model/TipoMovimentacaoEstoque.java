package com.levelupstore.estoque.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipos_movimentacao")
public class TipoMovimentacaoEstoque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao; // Ex: ENTRADA, VENDA, AJUSTE, PERDA

    public TipoMovimentacaoEstoque() {}
    public TipoMovimentacaoEstoque(String descricao) { this.descricao = descricao; }

    public Long getId() { return id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}