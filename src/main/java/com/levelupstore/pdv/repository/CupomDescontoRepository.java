package com.levelupstore.pdv.repository;

import com.levelupstore.pdv.model.CupomDesconto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CupomDescontoRepository extends JpaRepository<CupomDesconto, String> {
    Optional<CupomDesconto> findByCodigo(String codigo);
}