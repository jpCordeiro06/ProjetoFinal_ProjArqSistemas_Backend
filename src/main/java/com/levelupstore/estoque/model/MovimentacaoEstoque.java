package com.levelupstore.estoque.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacoes_estoque")
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime dataHora;

    private int quantidade;
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "estoque_id")
    private Estoque estoque;

    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private TipoMovimentacaoEstoque tipo;

    public MovimentacaoEstoque() {}

    // Getters e Setters
    public Long getId() {
        return id;
    }
    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public int getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    public String getObservacao() {
        return observacao;
    }
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    public Estoque getEstoque() {
        return estoque;
    }
    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }
    public TipoMovimentacaoEstoque getTipo() {
        return tipo;
    }
    public void setTipo(TipoMovimentacaoEstoque tipo) {
        this.tipo = tipo;
    }
}