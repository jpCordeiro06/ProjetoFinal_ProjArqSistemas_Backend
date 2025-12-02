package com.levelupstore.pdv.model;

import com.levelupstore.usuarios.model.Cliente;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cupons_desconto")
public class CupomDesconto {

    @Id
    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private BigDecimal percentual; // Pode representar valor fixo também

    private LocalDate validade;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "cliente_id") // Pode ser null (cupom genérico tipo NATAL10)
    private Cliente cliente;

    public CupomDesconto() {
    }

    // Getters e Setters
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

    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        }
}