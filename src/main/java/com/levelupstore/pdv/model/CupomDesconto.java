package com.levelupstore.pdv.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cupons_desconto")
public class CupomDesconto {

    @Id
    @Column(nullable = false, unique = true)
    private String codigo; // Ex: "NATAL10"

    @Column(nullable = false)
    private BigDecimal percentual; // Ex: 10.00

    private LocalDate validade; // Data de expiração

    // --- O CAMPO QUE FALTAVA ---
    @Column(nullable = false)
    private Boolean ativo = true;

    // Construtor Vazio (Obrigatório JPA)
    public CupomDesconto() {
    }

    // Getters e Setters Manuais
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getPercentual() {
        return percentual;
    }
    public void setPercentual(BigDecimal percentual) {
        this.percentual = percentual;
    }

    public LocalDate getValidade() {
        return validade;
    }
    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    public Boolean getAtivo() {
        return ativo;
    }
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}