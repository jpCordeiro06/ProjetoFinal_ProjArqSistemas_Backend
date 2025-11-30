package com.levelupstore.usuarios.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "perfis_administrador")
public class PerfilAdministrador extends PerfilUsuario {

    public PerfilAdministrador() {
        this.descricao = "Administrador";
    }

    @Override
    public List<String> obterAcoesPermitidas() {
        return Arrays.asList("GERENCIAR_ESTOQUE", "GERAR_RELATORIOS", "REALIZAR_VENDA", "GERENCIAR_USUARIOS");
    }
}