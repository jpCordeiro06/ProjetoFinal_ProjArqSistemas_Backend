package com.levelupstore.relatorios.service;

import com.levelupstore.estoque.model.Produto;
import com.levelupstore.estoque.repository.ProdutoRepository;
import com.levelupstore.pdv.model.Venda;
import com.levelupstore.pdv.repository.VendaRepository;
import com.levelupstore.relatorios.dto.RelatorioEstoqueDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RelatorioService {

    private final ProdutoRepository produtoRepository;
    private final VendaRepository vendaRepository;

    public RelatorioService(ProdutoRepository produtoRepository, VendaRepository vendaRepository) {
        this.produtoRepository = produtoRepository;
        this.vendaRepository = vendaRepository;
    }

    // 1. Gera o relatório financeiro do estoque atual
    public RelatorioEstoqueDTO gerarRelatorioEstoque() {
        List<Produto> produtos = produtoRepository.findAll();

        int totalItens = 0;
        BigDecimal valorTotal = BigDecimal.ZERO;
        String produtoMaisCaro = "Nenhum";
        BigDecimal maiorPreco = BigDecimal.ZERO;

        for (Produto p : produtos) {
            // MUDANÇA AQUI: Verifica se o produto tem o objeto Estoque associado
            if (p.getEstoque() != null && p.getEstoque().getQuantidade() != null) {

                // Acessa a quantidade através do objeto Estoque
                int qtd = p.getEstoque().getQuantidade();

                totalItens += qtd;

                // Calcula valor total (Preço * Quantidade)
                BigDecimal valorEstoqueProduto = p.getPreco().multiply(new BigDecimal(qtd));
                valorTotal = valorTotal.add(valorEstoqueProduto);
            }

            // Acha o mais caro
            if (p.getPreco().compareTo(maiorPreco) > 0) {
                maiorPreco = p.getPreco();
                produtoMaisCaro = p.getNome();
            }
        }

        return new RelatorioEstoqueDTO(totalItens, valorTotal, produtoMaisCaro);
    }

    // 2. Calcula o total de vendas realizadas (Simples)
    public BigDecimal calcularTotalVendas() {
        List<Venda> vendas = vendaRepository.findAll();
        BigDecimal total = BigDecimal.ZERO;

        for (Venda v : vendas) {
            total = total.add(v.getValorTotal());
        }
        return total;
    }
}