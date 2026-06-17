package com.pasfinal.Adaptadores.Servicos;

public class ItemEstoqueDTO {
    private Long id;
    private Integer quantidade;
    private Long ingredienteId;
    private String ingredienteDescricao;

    public ItemEstoqueDTO() {
    }

    public ItemEstoqueDTO(Long id, Integer quantidade, Long ingredienteId, String ingredienteDescricao) {
        this.id = id;
        this.quantidade = quantidade;
        this.ingredienteId = ingredienteId;
        this.ingredienteDescricao = ingredienteDescricao;
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

    public Long getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteId(Long ingredienteId) {
        this.ingredienteId = ingredienteId;
    }

    public String getIngredienteDescricao() {
        return ingredienteDescricao;
    }

    public void setIngredienteDescricao(String ingredienteDescricao) {
        this.ingredienteDescricao = ingredienteDescricao;
    }
}
