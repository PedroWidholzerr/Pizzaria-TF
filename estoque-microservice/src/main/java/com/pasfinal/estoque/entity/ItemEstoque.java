package com.pasfinal.estoque.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "itens_estoque")
public class ItemEstoque {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer quantidade;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ingrediente_id", nullable = false)
    private Ingrediente ingrediente;

    public ItemEstoque() {
    }

    public ItemEstoque(Long id, Integer quantidade, Ingrediente ingrediente) {
        this.id = id;
        this.quantidade = quantidade;
        this.ingrediente = ingrediente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }
}
