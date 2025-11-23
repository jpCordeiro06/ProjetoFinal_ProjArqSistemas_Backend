package com.levelupstore.estoque.service;

import com.levelupstore.estoque.model.Produto;
import com.levelupstore.estoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstoqueService {

    private final ProdutoRepository produtoRepository;

    @Autowired
    public EstoqueService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto salvarProduto(Produto produto) {

        // Exemplo
        // if (produto.getPreco().doubleValue() > 1000) {
        //     // Aplicar aprovação de gerente para itens de alto valor
        // }

        return produtoRepository.save(produto);
    }

    public List<Produto> buscarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }
}