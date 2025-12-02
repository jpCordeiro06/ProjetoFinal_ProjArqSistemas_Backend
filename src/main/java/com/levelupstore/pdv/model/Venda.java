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

    @ManyToOne
    @JoinColumn(name = "status_id")
    private StatusVenda statusVenda;

    @ManyToOne
    @JoinColumn(name = "cupom_id")
    private CupomDesconto cupomDesconto;


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

    public Venda() {
        // Data definida automaticamente pelo @CreationTimestamp ou aqui se preferir
        this.dataHora = LocalDateTime.now();
    }

    public void adicionarItem(ItemVenda item) {
        item.setVenda(this);
        this.itens.add(item);
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    // Método auxiliar para calcular o total com base nos itens
    public void calcularTotal() {
        this.valorTotal = BigDecimal.ZERO;
        if (this.itens != null) {
            for (ItemVenda item : this.itens) {
                // Validação de segurança para evitar erro se o preço for nulo
                if (item.getPrecoUnitario() != null) {
                    BigDecimal subtotal = item.getPrecoUnitario().multiply(new BigDecimal(item.getQuantidade()));
                    this.valorTotal = this.valorTotal.add(subtotal);
                }
            }
        }
    }

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