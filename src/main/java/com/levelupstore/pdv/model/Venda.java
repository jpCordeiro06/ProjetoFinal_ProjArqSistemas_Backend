package com.levelupstore.pdv.model;

import com.levelupstore.pagamentos.TipoPagamento;
import com.levelupstore.usuarios.model.Cliente;
import com.levelupstore.usuarios.model.Usuario;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendas")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataHora;

    private BigDecimal valorTotal = BigDecimal.ZERO;

    // --- NOVOS CAMPOS (Estrutura do Colega) ---

    @OneToOne
    @JoinColumn(name = "status_id")
    private StatusVenda statusVenda;

    @OneToOne
    @JoinColumn(name = "cupom_id")
    private CupomDesconto cupomDesconto;

    // --- CAMPOS ESSENCIAIS (Nossa Estrutura) ---

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tipo_pagamento_id") // Nome da coluna no banco
    private TipoPagamento tipoPagamento;

    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Usuario vendedor;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens = new ArrayList<>();

    // -------------------------------------------
    // CONSTRUTORES
    // -------------------------------------------

    public Venda() {
        // Data definida automaticamente pelo @CreationTimestamp ou aqui se preferir
        // this.dataHora = LocalDateTime.now();
    }

    // -------------------------------------------
    // MÉTODOS AUXILIARES
    // -------------------------------------------

    public void adicionarItem(ItemVenda item) {
        item.setVenda(this);
        this.itens.add(item);
        // O cálculo do total geralmente é feito no Service para considerar cupons,
        // mas podemos ter um método simples aqui também.
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    // -------------------------------------------
    // GETTERS E SETTERS
    // -------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public StatusVenda getStatusVenda() {
        return statusVenda;
    }

    public void setStatusVenda(StatusVenda statusVenda) {
        this.statusVenda = statusVenda;
    }

    public CupomDesconto getCupomDesconto() {
        return cupomDesconto;
    }

    public void setCupomDesconto(CupomDesconto cupomDesconto) {
        this.cupomDesconto = cupomDesconto;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }
}