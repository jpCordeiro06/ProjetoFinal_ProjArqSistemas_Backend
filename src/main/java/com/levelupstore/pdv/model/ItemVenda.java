package com.levelupstore.pdv.model;

import com.levelupstore.estoque.model.Produto;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "itens_venda")
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private BigDecimal precoUnitario; // O preço é gravado aqui para não mudar depois!

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;

    // Construtor Vazio (Obrigatório do JPA)
    public ItemVenda() {
    }

    // Já pega o preço atual do produto
    public ItemVenda(Produto produto, Integer quantidade) {
        setProduto(produto);
        setQuantidade(quantidade);
        // Regra de Negócio: O preço da venda é o preço ATUAL do produto
        this.precoUnitario = produto.getPreco();
    }

    // Método de Negócio: Calcular Subtotal
    public BigDecimal getSubtotal() {
        if (precoUnitario == null || quantidade == null) return BigDecimal.ZERO;
        return precoUnitario.multiply(new BigDecimal(quantidade));
    }


    public Long getId() {
        return id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }
    public void setPrecoUnitario(BigDecimal precoUnitario) {
        if (precoUnitario == null || precoUnitario.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço unitário não pode ser negativo.");
        }
        this.precoUnitario = precoUnitario;
    }

    public Produto getProduto() {
        return produto;
    }
    public void setProduto(Produto produto) {
        if (produto == null) throw new IllegalArgumentException("O produto é obrigatório.");
        this.produto = produto;
    }

    public Venda getVenda() {
        return venda;
    }
    public void setVenda(Venda venda) {
        this.venda = venda;
    }
}