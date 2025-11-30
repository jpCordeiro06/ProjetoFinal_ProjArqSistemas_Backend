package com.levelupstore.pagamentos;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "pagamentos_cartao")
public abstract class Cartao extends TipoPagamento {

    protected String bandeira; // Visa, Mastercard
    protected String numeroTransacao; // NSU

    public Cartao() {
    }

    // Simula a conexão com a maquininha
    public boolean autorizarTransacao() {
        System.out.println("Conectando com a operadora do cartão...");
        return true;
    }

    // Getters e Setters
    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public String getNumeroTransacao() {
        return numeroTransacao;
    }

    public void setNumeroTransacao(String numeroTransacao) {
        this.numeroTransacao = numeroTransacao;
    }
}