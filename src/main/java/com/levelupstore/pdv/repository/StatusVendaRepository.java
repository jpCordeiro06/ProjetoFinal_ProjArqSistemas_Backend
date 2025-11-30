package com.levelupstore.pdv.repository;

import com.levelupstore.pdv.model.StatusVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StatusVendaRepository extends JpaRepository<StatusVenda, Long> {
    Optional<StatusVenda> findByDescricao(String descricao);
}