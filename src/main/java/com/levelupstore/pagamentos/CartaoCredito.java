package com.levelupstore.pagamentos;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "pagamentos_credito")
public class CartaoCredito extends Cartao {

    private Integer numeroParcelas;

    public CartaoCredito() {
        this.descricao = "Cartão de Crédito";
    }

    @Override
    public boolean processarPagamento(double valor) {
        if (autorizarTransacao()) {
            System.out.println("Crédito aprovado em " + numeroParcelas + "x.");
            return true;
        }
        return false;
    }

    public Integer getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(Integer numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }
}