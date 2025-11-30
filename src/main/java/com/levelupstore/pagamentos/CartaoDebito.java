package com.levelupstore.pagamentos;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "pagamentos_debito")
public class CartaoDebito extends Cartao {

    private String tipoConta; // Corrente ou Poupança

    public CartaoDebito() {
        this.descricao = "Cartão de Débito";
    }

    @Override
    public boolean processarPagamento(double valor) {
        if (autorizarTransacao()) {
            System.out.println("Débito aprovado na conta " + tipoConta);
            return true;
        }
        return false;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }
}