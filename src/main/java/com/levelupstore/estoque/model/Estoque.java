package com.levelupstore.estoque.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estoques")
public class Estoque {

    @Id
    private Long id;

    // @MapsId significa: "O ID do Estoque é o MESMO ID do Produto"
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Produto produto;

    private Integer quantidade;

    public Estoque() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}