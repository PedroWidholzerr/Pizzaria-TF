package com.pasfinal.Dominio.Entidades;

public class Usuario {
    private Long id;
    private String username;
    private String password;
    private String tipo; // "USUARIO" ou "ADMIN"

    public Usuario() {}

    public Usuario(String username, String password, String tipo) {
        this.username = username;
        this.password = password;
        this.tipo = tipo;
    }

    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getTipo() { return tipo; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    // Métodos utilitários
    public boolean isAdmin() {
        return "ADMIN".equals(tipo);
    }

    public boolean isUsuario() {
        return "USUARIO".equals(tipo);
    }
}