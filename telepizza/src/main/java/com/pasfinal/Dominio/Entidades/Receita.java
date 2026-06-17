package com.pasfinal.Dominio.Entidades;

import java.util.List;

public class Receita {
    private long id;
    private String titulo;
    private List<Ingrediente> ingredientes;

    public Receita(long id, String titulo, List<Ingrediente> ingredientes) {
        this.id = id;
        this.titulo = titulo;
        this.ingredientes = ingredientes;
    }

    public long getId() { return id; }
    public String getTitulo(){ return titulo; }
    public List<Ingrediente> getIngredientes() { return ingredientes; }
}
