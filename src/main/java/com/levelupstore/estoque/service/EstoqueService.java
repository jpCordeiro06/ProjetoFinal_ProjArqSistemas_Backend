package com.levelupstore.estoque.service;

import com.levelupstore.exception.BusinessRuleException;
import com.levelupstore.estoque.model.Estoque;
import com.levelupstore.estoque.model.Produto;
import com.levelupstore.estoque.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EstoqueService {

    private final ProdutoRepository produtoRepository;

    public EstoqueService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Produto salvarProduto(Produto produto) {
        // 1. Validação de Nome Único
        if (produto.getId() == null) {
            List<Produto> existentes = produtoRepository.findByNomeContainingIgnoreCase(produto.getNome());
            if (!existentes.isEmpty()) {
                throw new BusinessRuleException("Já existe um produto com o nome '" + produto.getNome() + "'.");
            }
        }

        // 2. Lógica de Estoque Separado (A grande mudança!)
        // Se o produto chegou sem objeto Estoque, criamos um zerado ou com o valor informado
        if (produto.getEstoque() == null) {
            Estoque novoEstoque = new Estoque();
            novoEstoque.setQuantidade(0); // Começa zerado se não informado
            produto.setEstoque(novoEstoque); // O método setEstoque já faz o vínculo bidirecional
        } else {
            // Garante o vínculo se veio no JSON
            produto.setEstoque(produto.getEstoque());
        }

        return produtoRepository.save(produto);
    }

    public List<Produto> buscarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }
}