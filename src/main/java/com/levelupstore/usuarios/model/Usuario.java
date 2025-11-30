package com.levelupstore.usuarios.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "perfil_id", referencedColumnName = "id")
    private PerfilUsuario perfil;

    public Usuario() {
    }

    public boolean temPermissao(String acao) {
        if (this.perfil == null) return false;
        return this.perfil.verificarPermissao(acao);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) throw new IllegalArgumentException("Nome é obrigatório");
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if (login == null || login.trim().isEmpty()) throw new IllegalArgumentException("Login é obrigatório");
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        if (senha == null || senha.length() < 6) throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres");
        this.senha = senha;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        if (perfil == null) throw new IllegalArgumentException("Perfil é obrigatório");
        this.perfil = perfil;
    }
}