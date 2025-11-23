package com.levelupstore.estoque.repository;

import com.levelupstore.estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Método que o Spring implementa automaticamente para buscar produtos por nome
    List<Produto> findByNomeContainingIgnoreCase(String nome);
}