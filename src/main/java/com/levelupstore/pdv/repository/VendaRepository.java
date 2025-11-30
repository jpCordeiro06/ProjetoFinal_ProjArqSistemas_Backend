package com.levelupstore.pdv.repository;

import com.levelupstore.pdv.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    // 1. Busca o histórico de compras de um cliente específico
    List<Venda> findByClienteId(Long clienteId);

    // 2. Busca todas as vendas feitas por um vendedor específico
    List<Venda> findByVendedorId(Long vendedorId);

    // 3. Busca vendas em um intervalo de tempo
    List<Venda> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
}