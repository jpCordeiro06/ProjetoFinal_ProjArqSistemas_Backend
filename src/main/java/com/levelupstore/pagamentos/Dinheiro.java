package com.levelupstore.pagamentos;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "pagamentos_dinheiro")
public class Dinheiro extends TipoPagamento {

    public Dinheiro() {
        this.descricao = "Dinheiro";
    }

    @Override
    public boolean processarPagamento(double valor) {
        // Lógica simplificada: Dinheiro é sempre aprovado na hora
        System.out.println("Recebendo R$ " + valor + " em espécie.");
        return true;
    }

    // Método específico do Dinheiro
    public double calcularTroco(double valorRecebido, double totalVenda) {
        return valorRecebido - totalVenda;
    }
}