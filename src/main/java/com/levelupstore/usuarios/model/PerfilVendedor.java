package com.levelupstore.usuarios.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "perfis_vendedor")
public class PerfilVendedor extends PerfilUsuario {

    public PerfilVendedor() {
        this.descricao = "Vendedor";
    }

    @Override
    public List<String> obterAcoesPermitidas() {
        // Vendedor tem permissões mais restritas que o Admin
        return Arrays.asList("REALIZAR_VENDA", "CONSULTAR_ESTOQUE", "CADASTRAR_CLIENTE");
    }
}