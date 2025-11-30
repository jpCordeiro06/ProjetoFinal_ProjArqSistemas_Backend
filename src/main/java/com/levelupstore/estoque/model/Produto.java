package com.levelupstore.estoque.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "produtos")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(name = "qtd_estoque")
    private Integer quantidadeEstoque;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condicao_id", nullable = false)
    private Condicao condicao;

    @OneToOne(mappedBy = "produto", cascade = CascadeType.ALL)
    private Estoque estoque;

    public Produto() {
    }

    // Método de Negócio: Atualiza o estoque
    public void atualizarEstoque(int quantidade) {
        if (this.quantidadeEstoque == null) {
            this.quantidadeEstoque = 0;
        }
        this.quantidadeEstoque += quantidade;
    }

    public Long getId() {
        return id;
    }
    public String getNome() {

        return nome;
    }
    public BigDecimal getPreco() {
        return preco;
    }

    public Estoque getEstoque() { return estoque; }
    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
        if (estoque != null) estoque.setProduto(this);
    }

    public Categoria getCategoria() {

        return categoria;
    }
    public Condicao getCondicao() {
        return condicao;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        this.nome = nome;
    }

    public void setPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço deve ser positivo.");
        }
        this.preco = preco;
    }

    public void setCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("A categoria é obrigatória.");
        }
        this.categoria = categoria;
    }

    public void setCondicao(Condicao condicao) {
        if (condicao == null) {
            throw new IllegalArgumentException("A condição é obrigatória.");
        }
        this.condicao = condicao;
    }
}