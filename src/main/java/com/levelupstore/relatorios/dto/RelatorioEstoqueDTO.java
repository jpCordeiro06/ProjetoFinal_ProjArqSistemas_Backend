package com.levelupstore.relatorios.dto;

import java.math.BigDecimal;

public class RelatorioEstoqueDTO {

    private int totalItens;
    private BigDecimal valorTotalEstoque;
    private String produtoMaisCaro;

    // Construtor
    public RelatorioEstoqueDTO(int totalItens, BigDecimal valorTotalEstoque, String produtoMaisCaro) {
        this.totalItens = totalItens;
        this.valorTotalEstoque = valorTotalEstoque;
        this.produtoMaisCaro = produtoMaisCaro;
    }

    // Getters manuais
    public int getTotalItens() {
        return totalItens;
    }
    public BigDecimal getValorTotalEstoque() {
        return valorTotalEstoque;
    }
    public String getProdutoMaisCaro() {
        return produtoMaisCaro;
    }
}