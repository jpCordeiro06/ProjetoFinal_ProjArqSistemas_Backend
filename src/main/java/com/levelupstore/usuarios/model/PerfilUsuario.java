package com.levelupstore.usuarios.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "perfis_usuario")
public abstract class PerfilUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    protected String descricao;

    public PerfilUsuario() {
    }

    // Método que obriga os filhos a definirem suas permissões
    @Transient
    public abstract List<String> obterAcoesPermitidas();

    public boolean verificarPermissao(String acao) {
        return obterAcoesPermitidas().contains(acao);
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}