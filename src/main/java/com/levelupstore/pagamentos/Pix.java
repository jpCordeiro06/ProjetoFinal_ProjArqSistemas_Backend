package com.levelupstore.pagamentos;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "pagamentos_pix")
public class Pix extends TipoPagamento {

    private String txid; // ID da transação no banco

    public Pix() {
        this.descricao = "Pix";
    }

    @Override
    public boolean processarPagamento(double valor) {
        System.out.println("Verificando recebimento do PIX de R$ " + valor + "...");
        return true;
    }

    public String gerarQrCode(double valor) {
        this.txid = UUID.randomUUID().toString();
        return "00020126580014BR.GOV.BCB.PIX..." + this.txid; // Simulação
    }

    // Getters e Setters
    public String getTxid() {
        return txid;
    }
    public void setTxid(String txid) {
        this.txid = txid;
    }
}