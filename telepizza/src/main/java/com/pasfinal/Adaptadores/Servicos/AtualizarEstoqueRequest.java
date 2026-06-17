package com.pasfinal.Adaptadores.Servicos;

public class AtualizarEstoqueRequest {
    private Long ingredienteId;
    private Integer quantidade;

    public AtualizarEstoqueRequest() {
    }

    public AtualizarEstoqueRequest(Long ingredienteId, Integer quantidade) {
        this.ingredienteId = ingredienteId;
        this.quantidade = quantidade;
    }

    public Long getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteId(Long ingredienteId) {
        this.ingredienteId = ingredienteId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
